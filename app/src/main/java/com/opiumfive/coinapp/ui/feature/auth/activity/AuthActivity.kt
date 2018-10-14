package com.opiumfive.coinapp.ui.feature.auth.activity

import android.arch.lifecycle.Observer
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_auth.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import tech.snowfox.betholder.R
import com.opiumfive.coinapp.data.serverModel.auth.AuthResponse
import com.opiumfive.coinapp.data.uiModel.data.Data
import com.opiumfive.coinapp.data.uiModel.data.Status
import com.opiumfive.coinapp.domain.di.component.AppComponent
import com.opiumfive.coinapp.extension.getChildViews
import com.opiumfive.coinapp.extension.getColorCompat
import com.opiumfive.coinapp.extension.setVisibility
import com.opiumfive.coinapp.ui.base.ActivityResultBaseActivity
import com.opiumfive.coinapp.ui.feature.auth.model.AuthViewModel
import com.opiumfive.coinapp.ui.feature.auth.view.LoginEmaiView
import com.opiumfive.coinapp.ui.feature.auth.view.LoginView
import com.opiumfive.coinapp.ui.feature.auth.view.RegisterView
import com.opiumfive.coinapp.ui.feature.main.activity.KEY_SHOW_ERROR_MESSAGE
import com.opiumfive.coinapp.ui.feature.main.activity.MainActivity

class AuthActivity : ActivityResultBaseActivity() {

    private val viewModel by lazy { initViewModel<AuthViewModel>() }

    private val loginObserver by lazy {
        Observer<Data<AuthResponse>> {
            it ?: return@Observer
            handleResult(it)
        }
    }

    private val loginView by lazy {
        LoginView(this).apply {
            loginListener = { socialType ->
                viewModel.loginSocial(this@AuthActivity, socialType)
                    .observe(this@AuthActivity, loginObserver)
            }
            emailListener = { setState(State.EMAIL) }
        }
    }

    private val loginEmailView by lazy {
        LoginEmaiView(this).apply {
            loginClicker = { email, password ->
                viewModel.login(email, password).observe(this@AuthActivity, loginObserver)
            }
        }
    }

    private val registerView by lazy {
        RegisterView(this).apply {
            registerListener = { name, email, password ->
                viewModel.register(name, email, password).observe(this@AuthActivity, loginObserver)
            }
        }
    }

    private enum class State {
        LOGIN,
        EMAIL,
        REGISTER,
        ADD_PHOTO,
        PASSWORD_RESET,
        PASSWORD_FORGOT,
        PASSWORD_FORGOT_SEND
    }

    private var state: State = State.LOGIN

    override fun injectDI(appComponent: AppComponent) = appComponent.inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        authBack.setOnClickListener { onBackPressed() }

        authRegister.setOnClickListener { setState(State.REGISTER) }

        changeStatusBarColor()
        setState(State.LOGIN)
    }

    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window?.statusBarColor = getColorCompat(R.color.lightBlue)
        }
    }

    private fun setState(state: State) {
        this.state = state
        authForgot.setVisibility(state == State.EMAIL)
        authRegister.setVisibility(state == State.EMAIL)
        authContainer.removeAllViews()
        when (state) {
            State.LOGIN -> authContainer.addView(loginView)
            State.EMAIL -> authContainer.addView(loginEmailView)
            State.REGISTER -> authContainer.addView(registerView)
            else -> {}
        }
    }

    private fun handleResult(data: Data<AuthResponse>) {
        authContainer.getChildViews().forEach { it.isEnabled = data.isLoading().not() }
        when (data.status) {
            Status.SUCCESS -> {
                val throwableMessage = data.throwable?.message
                startActivity<MainActivity>(KEY_SHOW_ERROR_MESSAGE to throwableMessage)
                finish()
            }
            Status.FAILURE -> {
                data.throwable?.message?.let { toast(it) }
            }
        }
    }

    override fun onBackPressed() {
        val stateOrdinal = state.ordinal - 1
        if (stateOrdinal < 0) {
            super.onBackPressed()
            return
        }
        setState(State.values()[stateOrdinal])
    }
}
