package com.opiumfive.coinapp.data.uiModel.wallet

data class WalletModel(
    val id: Int,
    var walletName: String,
    val address: String,
    val type: WalletType,
    val totalBalance: String,
    val balanceUsd: String,
    val balance: String,
    val tokenBalance: String,
    val pushEnabled: Boolean
)