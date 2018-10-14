package com.opiumfive.coinapp.data.serverModel.wallet

import com.google.gson.annotations.SerializedName

data class WalletResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String?,
    @SerializedName("address")
    val address: String?,
    @SerializedName("symbol")
    val symbol: String?,
    @SerializedName("balance")
    val balance: String?,
    @SerializedName("balance_usd")
    val balanceUsd: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("tokens_balance_usd")
    val tokenBalance: String?,
    @SerializedName("total_balance_usd")
    val totalBalance: String?,
    @SerializedName("no_push")
    val noPush: String?
)