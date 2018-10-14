package com.opiumfive.coinapp.domain.network

import android.arch.lifecycle.LiveData
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import com.opiumfive.coinapp.data.serverModel.BaseResponse
import com.opiumfive.coinapp.data.serverModel.apiResponse.ApiResponse
import com.opiumfive.coinapp.data.serverModel.auth.AuthResponse
import com.opiumfive.coinapp.data.serverModel.main.MainBalanceResponse
import com.opiumfive.coinapp.data.serverModel.transactions.TransactionsResponse
import com.opiumfive.coinapp.data.serverModel.wallet.NewWalletResponse
import com.opiumfive.coinapp.domain.DomainUtils

interface ApiService {

    @GET("users/balance")
    fun getMainBalance(): LiveData<ApiResponse<MainBalanceResponse>>

    @GET("transactions")
    fun getTransactions(
        @Query("wallet_id") walletId: Int,
        @Query("timestamp") timestamp: Long,
        @Query("per_page") perPage: Int
    ): LiveData<ApiResponse<List<TransactionsResponse>>>

    @POST("users/login")
    fun login(
        @Query("email") email: String,
        @Query("password") password: String,
        @Query("device_identifier") deviceIdentifier: String = DomainUtils.getDeviceId(),
        @Query("details") details: String = DomainUtils.getDeviceDetails(),
        @Query("user_type") userType: String = "wallet_tracker_bl",
        @Query("device_platform") platform: String = "android"
    ): LiveData<ApiResponse<AuthResponse>>

    @POST("users/login_social")
    fun loginSocial(
        @Query("social_token") token: String,
        @Query("social_network") socialType: String,
        @Query("device_identifier") deviceIdentifier: String = DomainUtils.getDeviceId(),
        @Query("details") details: String = DomainUtils.getDeviceDetails(),
        @Query("user_type") userType: String = "wallet_tracker_bl",
        @Query("device_platform") platform: String = "android"
    ): LiveData<ApiResponse<AuthResponse>>

    @POST("users/register")
    fun register(
        @Query("email") email: String,
        @Query("password") password: String,
        @Query("name") name: String,
        @Query("device_identifier") deviceIdentifier: String = DomainUtils.getDeviceId(),
        @Query("details") details: String = DomainUtils.getDeviceDetails(),
        @Query("user_type") userType: String = "wallet_tracker_bl",
        @Query("device_platform") platform: String = "android"
    ): LiveData<ApiResponse<AuthResponse>>

    @PUT("users/update_password")
    fun updatePassword(
        @Query("code") code: String,
        @Query("new_password") password: String,
        @Query("device_identifier") deviceIdentifier: String = DomainUtils.getDeviceId(),
        @Query("details") details: String = DomainUtils.getDeviceDetails(),
        @Query("user_type") userType: String = "wallet_tracker_bl",
        @Query("device_platform") platform: String = "android"
    ): LiveData<ApiResponse<BaseResponse>>

    @POST("users/reset_password")
    fun resetPassword(
        @Query("email") email: String,
        @Query("device_identifier") deviceIdentifier: String = DomainUtils.getDeviceId(),
        @Query("details") details: String = DomainUtils.getDeviceDetails(),
        @Query("device_platform") platform: String = "android"
    ): LiveData<ApiResponse<BaseResponse>>

    @POST("wallets")
    fun newWallet(
        @Query("name") name: String,
        @Query("address") address: String,
        @Query("wallet_type") walletType: String
    ): LiveData<ApiResponse<NewWalletResponse>>
}