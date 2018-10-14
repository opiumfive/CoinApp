package com.opiumfive.coinapp.ui.feature.newWallet.model

import android.arch.lifecycle.ViewModel
import com.opiumfive.coinapp.data.uiModel.wallet.WalletType
import com.opiumfive.coinapp.domain.useCase.NewWalletUseCase
import javax.inject.Inject

class NewWalletViewModel @Inject constructor(private val useCase: NewWalletUseCase): ViewModel() {

    fun checkClipboard() = useCase.checkClipboard(false)

    fun isAddressValid(address: String?, walletType: WalletType?): Boolean {
        return useCase.isAddressValid(address, walletType)
    }

    fun getClipboardWallet(address: String?) = useCase.getClipboardWallet(address)

    fun addPostponedWallet(
        address: String,
        walletName: String,
        walletType: WalletType
    ) = useCase.addPostponedWallet(address, walletName, walletType)

    fun addWallet(
        address: String,
        walletName: String,
        walletType: WalletType
    ) = useCase.addWallet(address, walletName, walletType)
}