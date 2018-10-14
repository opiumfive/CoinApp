package com.opiumfive.coinapp.data.bdModel.wallet

import com.opiumfive.coinapp.data.uiModel.wallet.WalletType

data class PostponedWallet(
    var address: String,
    var walletName: String,
    var walletType: WalletType
) {

    fun wrap(): String {
        return "$address-$walletName-$walletType"
    }

    companion object {
        fun unwrap(wrapString: String?): PostponedWallet? {
            val wrap = wrapString?.split("-") ?: return null
            val address = wrap.getOrNull(0) ?: ""
            val walletName = wrap.getOrNull(1) ?: ""
            val walletType = try {
                WalletType.valueOf(wrap.getOrNull(2) ?: "")
            } catch (e: Exception) {
                null
            }

            if (address.isEmpty() || walletName.isEmpty() || walletType == null) return null
            return PostponedWallet(address, walletName, walletType)
        }
    }
}