package com.opiumfive.coinapp.domain.useCase

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.content.Context
import android.net.Uri
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues
import tech.snowfox.betholder.R
import com.opiumfive.coinapp.data.serverModel.BaseResponse
import com.opiumfive.coinapp.data.serverModel.apiResponse.ApiResponse
import com.opiumfive.coinapp.data.serverModel.auth.AuthResponse
import com.opiumfive.coinapp.data.uiModel.data.Data
import com.opiumfive.coinapp.domain.DomainUtils
import com.opiumfive.coinapp.domain.Prefs
import com.opiumfive.coinapp.domain.network.ApiService
import com.opiumfive.coinapp.domain.network.GoogleApiService
import com.opiumfive.coinapp.domain.network.mapper.ApiResponseDataMapper
import com.opiumfive.coinapp.domain.repository.wallet.IWalletRepository
import com.opiumfive.coinapp.domain.throwable.CreateWalletThrowable
import com.opiumfive.coinapp.domain.throwable.NoInternetThrowable
import com.opiumfive.coinapp.domain.throwable.WrongArgumentsThrowable
import com.opiumfive.coinapp.domain.throwable.auth.CancelThrowable
import com.opiumfive.coinapp.domain.throwable.auth.EmptyThrowable
import com.opiumfive.coinapp.domain.throwable.auth.FacebookThrowable
import com.opiumfive.coinapp.domain.throwable.auth.GoogleThrowable
import com.opiumfive.coinapp.extension.checkPlayServices
import com.opiumfive.coinapp.extension.getString
import com.opiumfive.coinapp.ui.base.ActivityResultBaseActivity
import javax.inject.Inject

private const val CLIENT_SERVER_ID =
    "542466035130-kf75q66d4ajcsho5f96noj3ut23glb0g.apps.googleusercontent.com"
private const val CLIENT_SECRET = "4F_gac7XzXLAOLlhUJU2pRP0"

private const val ICOS_CLIENT_ID = "63146e67-ae1f-34a1-84fa-35015009763c"
private const val ICOS_SECRET = "FbUNlWJzXBFZKJ9aPodpObpTJMInA5B7"
private const val ICOS_REDIRECT_URI = "betholdericosid://authorize"

private const val EMAIL = "email"
private const val GOOGLE = "google"
private const val FACEBOOK = "facebook"
private const val ICOS_ID = "icos"

class AuthUseCase
@Inject constructor(
    private val apiService: ApiService,
    private val googleService: GoogleApiService,
    private val context: Context,
    private val prefs: Prefs,
    private val walletRepository: IWalletRepository
) {
    enum class SocialType {
        FACEBOOK,
        GOOGLE,
        ICOS_ID
    }

    fun login(email: String?, password: String?): LiveData<Data<AuthResponse>> {
        val liveData = MediatorLiveData<Data<AuthResponse>>()
        liveData.postValue(Data.loading())

        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            liveData.postValue(Data.errorThrowable(EmptyThrowable()))
            return liveData
        }

        if (isValidEmail(email).not()) {
            sendWrongEmail(liveData)
            return liveData
        }

        liveData.addSource(apiService.login(email, password)) {
            prefs.saveLogin(email)
            holdAuthResponse(liveData, it)
        }
        return liveData
    }

    fun loginSocial(
        activity: ActivityResultBaseActivity,
        socialType: SocialType
    ): LiveData<Data<AuthResponse>> {
        return when (socialType) {
            SocialType.GOOGLE -> loginGoogle(activity)
            SocialType.FACEBOOK -> loginFacebook(activity)
            SocialType.ICOS_ID -> loginIcosId(activity)
        }
    }

    fun loginFacebook(activity: ActivityResultBaseActivity): LiveData<Data<AuthResponse>> {
        val liveData = MediatorLiveData<Data<AuthResponse>>()
        liveData.postValue(Data.loading())

        loginFacebook(activity, liveData)

        return liveData
    }

    private fun loginFacebook(
        activity: ActivityResultBaseActivity,
        liveData: MediatorLiveData<Data<AuthResponse>>
    ) {
        val callbackManager = CallbackManager.Factory.create()
        val loginManager = LoginManager.getInstance()
        val facebookCallback = object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                val token = result?.accessToken?.token
                loginManager.logOut()
                when (token) {
                    null -> liveData.postValue(Data.errorThrowable(FacebookThrowable()))

                    else -> loginSocial(liveData, token, FACEBOOK)
                }
            }

            override fun onCancel() {
                liveData.postValue(Data.errorThrowable(CancelThrowable()))
            }

            override fun onError(error: FacebookException?) {
                loginManager.logOut()
                liveData.postValue(Data.errorThrowable(FacebookThrowable(error?.message)))
            }
        }

        activity.activityResultListener = { requestCode, resultCode, data ->
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }

        loginManager.registerCallback(callbackManager, facebookCallback)
        loginManager.logInWithReadPermissions(activity, listOf(EMAIL))
    }

    private fun loginGoogle(activity: ActivityResultBaseActivity): LiveData<Data<AuthResponse>> {
        val liveData = MediatorLiveData<Data<AuthResponse>>()
        liveData.postValue(Data.loading())

        if (context.checkPlayServices().not()) {
            liveData.postValue(
                Data.errorThrowable(
                    GoogleThrowable(getString(R.string.error_google_services_unavailable))
                )
            )
            return liveData
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode(CLIENT_SERVER_ID)
            .requestEmail()
            .build()

        val apiClient = GoogleSignIn.getClient(activity, gso)
        val signInIntent = apiClient.signInIntent

        if (signInIntent.resolveActivity(activity.packageManager) == null) {
            liveData.postValue(
                Data.errorThrowable(
                    GoogleThrowable(getString(R.string.error_google_services_unavailable))
                )
            )
            return liveData
        }

        activity.startActivityWithResult(signInIntent, 1, success = {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(it)
            apiClient.signOut()
            if (result.isSuccess) {
                val acc = result.signInAccount
                val authCode = acc?.serverAuthCode ?: return@startActivityWithResult
                getAccessToken(liveData, authCode)
            } else {
                liveData.postValue(
                    Data.errorThrowable(GoogleThrowable(getString(R.string.error_no_internet)))
                )
            }
        }, canceled = {
            liveData.postValue(Data.errorThrowable(CancelThrowable()))
        })

        return liveData
    }

    private fun getAccessToken(liveData: MediatorLiveData<Data<AuthResponse>>, authCode: String) {
        liveData.addSource(googleService.login(CLIENT_SERVER_ID, CLIENT_SECRET, authCode)) {
            val accessToken = it?.body?.accessToken
            when (accessToken) {
                null -> liveData.postValue(Data.errorThrowable(it?.throwable))
                else -> loginSocial(liveData, accessToken, GOOGLE)
            }
        }
    }

    private fun loginIcosId(activity: ActivityResultBaseActivity): LiveData<Data<AuthResponse>> {
        val liveData = MediatorLiveData<Data<AuthResponse>>()
        liveData.postValue(Data.loading())

        val service = AuthorizationServiceConfiguration(
            Uri.parse("https://icosid.com/oauth2/auth"),
            Uri.parse("https://icosid.com/oauth2/token")
        )
        val request = AuthorizationRequest.Builder(
            service,
            ICOS_CLIENT_ID,
            ResponseTypeValues.TOKEN,
            Uri.parse(ICOS_REDIRECT_URI)
        ).setScope("user.data").build()
        val authService = AuthorizationService(activity)
        val intent = authService.getAuthorizationRequestIntent(request)
        activity.startActivityWithResult(intent, 1, success = {
            authService.dispose()

            val error = try {
                val uri = it?.data.toString()
                val prefix = "$ICOS_REDIRECT_URI#error="
                if (uri.contains(prefix).not()) {
                    null
                } else {
                    val noPrefixUri = uri.substring(uri.indexOf(prefix) + prefix.length)
                    noPrefixUri.split("&").getOrNull(0)?.replace("_", " ")
                }
            } catch (e: Exception) {
                null
            }

            if (error.isNullOrEmpty().not()) {
                liveData.postValue(Data.errorThrowable(Throwable(error)))
                return@startActivityWithResult
            }

            val token = try {
                val uri = it?.data.toString()
                val prefix = "$ICOS_REDIRECT_URI#access_token="
                val noPrefixUri = uri.substring(uri.indexOf(prefix) + prefix.length)
                noPrefixUri.split("&").getOrNull(0)
            } catch (e: Exception) {
                null
            }

            when (token) {
                null -> {
                    liveData.postValue(
                        Data.errorThrowable(Throwable(getString(R.string.error_no_internet)))
                    )
                }

                else -> {
                    loginSocial(liveData, token, ICOS_ID)
                }
            }
        }, canceled = {
            authService.dispose()
            liveData.postValue(Data.errorThrowable(CancelThrowable()))
        })

        return liveData
    }

    private fun loginSocial(
        liveData: MediatorLiveData<Data<AuthResponse>>,
        token: String,
        socialType: String
    ) {
        liveData.addSource(apiService.loginSocial(token, socialType)) {
            holdAuthResponse(liveData, it)
        }
    }

    fun registration(
        name: String?,
        email: String?,
        password: String?
    ): LiveData<Data<AuthResponse>> {
        val liveData = MediatorLiveData<Data<AuthResponse>>()
        liveData.postValue(Data.loading())

        if (name == null || name.isEmpty() ||
            email == null || email.isEmpty() ||
            password == null || password.isEmpty()
        ) {
            liveData.postValue(Data.errorThrowable(EmptyThrowable()))
            return liveData
        }

        if (isValidEmail(email).not()) {
            sendWrongEmail(liveData)
            return liveData
        }

        liveData.addSource(apiService.register(email, password, name)) {
            holdAuthResponse(liveData, it)
        }
        return liveData
    }

    private fun <T> sendWrongEmail(liveData: MediatorLiveData<Data<T>>) {
        val errorMessage = context.getString(R.string.error_login_email)
        val errorData = Data.errorThrowable(WrongArgumentsThrowable(errorMessage))
        liveData.postValue(errorData)
    }

    fun isValidEmail(email: String) = email.contains("@", true)

    private fun holdAuthResponse(liveData: MediatorLiveData<Data<AuthResponse>>, apiResponse: ApiResponse<AuthResponse>?) {
        handleNoInternetThrowable(apiResponse)
        val data = ApiResponseDataMapper.map(apiResponse)
        if (apiResponse?.isSuccess() == true){
            saveToken(apiResponse.body)
            liveData.addSource(walletRepository.uploadPostponedWallet()) {
                if (it?.isLoading() == true) return@addSource

                if (it?.isError() == true){
                    data.throwable = CreateWalletThrowable(it.throwable?.message)
                }
                liveData.postValue(data)
            }
        } else {
            liveData.postValue(data)
        }
    }

    private fun saveToken(authResponse: AuthResponse?) {
        prefs.saveToken(authResponse?.token)
    }

    fun forgot(email: String): LiveData<Data<BaseResponse>> {
        val liveData = MediatorLiveData<Data<BaseResponse>>()
        liveData.postValue(Data.loading())

        if (isValidEmail(email).not()) {
            sendWrongEmail(liveData)
            return liveData
        }

        liveData.addSource(apiService.resetPassword(email)) {
            handleNoInternetThrowable(it)
            liveData.postValue(ApiResponseDataMapper.map(it))
        }
        return liveData
    }

    fun restore(code: String, login: String, password: String): LiveData<Data<BaseResponse>> {
        val liveData = MediatorLiveData<Data<BaseResponse>>()
        liveData.postValue(Data.loading())

        liveData.addSource(apiService.updatePassword(code, login, password)) {
            handleNoInternetThrowable(it)
            liveData.postValue(ApiResponseDataMapper.map(it))
        }

        return liveData
    }

    fun logout(): LiveData<Data<BaseResponse>> {
        val liveData = MediatorLiveData<Data<BaseResponse>>()
        liveData.postValue(Data.loading())
        prefs.saveToken(null)
        // Stub
        liveData.postValue(Data.success(null))
        return liveData
    }

    fun getLogin() = prefs.getLogin()

    private fun handleNoInternetThrowable(response: ApiResponse<*>?) {
        if (response?.throwable is NoInternetThrowable) {
            response.throwable = NoInternetThrowable(getString(R.string.error_no_internet_action))
        }
    }
}