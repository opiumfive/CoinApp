package com.opiumfive.coinapp.data.serverModel.wallet

import com.google.gson.annotations.SerializedName

data class NewWalletResponse(
    @SerializedName("code")
    val code: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("wallet_id")
    val walletId: String?
)
