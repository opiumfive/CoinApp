package com.opiumfive.coinapp.domain.network

import android.arch.lifecycle.LiveData
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST
import com.opiumfive.coinapp.data.serverModel.apiResponse.ApiResponse
import com.opiumfive.coinapp.data.serverModel.auth.GoogleAuthResponse

interface GoogleApiService {
    @POST("oauth2/v4/token")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    fun login(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") authCode: String,
        @Field("grant_type") type: String = "authorization_code"
    ): LiveData<ApiResponse<GoogleAuthResponse>>
}