package com.opiumfive.coinapp.data.serverModel.auth

import com.google.gson.annotations.SerializedName

data class GoogleAuthResponse(
    @SerializedName("access_token")
    val accessToken: String
)