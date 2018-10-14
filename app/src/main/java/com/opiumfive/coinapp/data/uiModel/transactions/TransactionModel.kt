package com.opiumfive.coinapp.data.uiModel.transactions

data class TransactionsModel(
    val id: Long,
    var timestamp: Long = 0,
    val walletId: Int,
    val address: String,
    val amount: Double,
    val fees: Double,
    val transactionDate: String,
    val direction: String,
    val confirmations: Int,
    val walletType: String
)