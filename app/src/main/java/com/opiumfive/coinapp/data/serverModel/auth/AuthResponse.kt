package com.opiumfive.coinapp.data.serverModel.auth

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("api_key")
    val token: String? = null,
    val code: String? = null,
    val message: String? = null
)