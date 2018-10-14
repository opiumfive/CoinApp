package com.opiumfive.coinapp.domain.mapper

import com.opiumfive.coinapp.data.bdModel.main.MainBalanceBdModel
import com.opiumfive.coinapp.data.serverModel.main.MainBalanceResponse
import com.opiumfive.coinapp.data.uiModel.main.MainBalanceModel

object MainBalanceMapper {

    fun map(mainBalanceResponse: MainBalanceResponse): MainBalanceModel {
        return MainBalanceModel(
            tokenBalance = if (mainBalanceResponse.tokensUsd != null) "${mainBalanceResponse.tokensUsd} USD" else  "0 USD",
            totalBalance = mainBalanceResponse.totalPortfolioValues ?: "0.0",
            btcBalance = if (mainBalanceResponse.btcBalance != null) "${mainBalanceResponse.btcBalance} BTC" else  "0 BTC",
            ethBalance = if (mainBalanceResponse.ethBalance != null) "${mainBalanceResponse.ethBalance} ETH" else  "0 ETH",
            currency = "$",
            income = ""
        )
    }

    fun map(mainBalanceBdModel: MainBalanceBdModel): MainBalanceModel {
        return MainBalanceModel(
            tokenBalance = "${mainBalanceBdModel.tokenBalance} USD",
            totalBalance = mainBalanceBdModel.totalBalance,
            btcBalance = "${mainBalanceBdModel.btcBalance} BTC",
            ethBalance = "${mainBalanceBdModel.ethBalance} ETH",
            currency = mainBalanceBdModel.currency,
            income = mainBalanceBdModel.income
        )
    }

    fun mapBd(mainBalanceResponse: MainBalanceResponse): MainBalanceBdModel {
        return MainBalanceBdModel(
            tokenBalance = mainBalanceResponse.tokensUsd ?: "",
            totalBalance = mainBalanceResponse.totalPortfolioValues ?: "",
            btcBalance = mainBalanceResponse.btcBalance ?: "",
            ethBalance = mainBalanceResponse.ethBalance ?: "",
            currency = "$",
            income = ""
        )
    }
}