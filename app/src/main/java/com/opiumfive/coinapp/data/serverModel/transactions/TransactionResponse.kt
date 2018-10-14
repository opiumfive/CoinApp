package com.opiumfive.coinapp.data.serverModel.transactions

import com.google.gson.annotations.SerializedName

data class TransactionsResponse(
    @SerializedName("id")
    val id: Long?,
    @SerializedName("wallet_id")
    val walletId: Int?,
    @SerializedName("address")
    val address: String?,
    @SerializedName("amount")
    val amount: Double?,
    @SerializedName("fees")
    val fees: Double?,
    @SerializedName("transaction_date")
    val transactionDate: String?,
    @SerializedName("direction")
    val direction: String?,
    @SerializedName("confirmations")
    val confirmations: Int?,
    @SerializedName("wallet_type")
    val walletType: String?
)