package com.opiumfive.coinapp.data.uiModel.wallet

import tech.snowfox.betholder.R
import com.opiumfive.coinapp.extension.getString

enum class WalletType {
    ETH,
    BTC;

    fun getTitle(): String {
        return when (this) {
            ETH -> getString(R.string.eth)
            BTC -> getString(R.string.btc)
        }
    }
}