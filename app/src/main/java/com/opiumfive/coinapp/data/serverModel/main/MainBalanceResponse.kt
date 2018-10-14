package com.opiumfive.coinapp.data.serverModel.main

import com.google.gson.annotations.SerializedName

data class MainBalanceResponse(
    @SerializedName("total_portfolio_values")
    val totalPortfolioValues: String?,
    @SerializedName("btc_balance")
    val btcBalance: String?,
    @SerializedName("eth_balance")
    val ethBalance: String?,
    @SerializedName("btc_balance_usd")
    val btcBalanceUsd: String?,
    @SerializedName("eth_balance_usd")
    val ethBalanceUsd: String?,
    @SerializedName("tokens_usd")
    val tokensUsd: String?
)
