package com.opiumfive.coinapp.ui.feature.newWallet.model

import android.arch.lifecycle.ViewModel
import com.opiumfive.coinapp.domain.useCase.NewWalletUseCase
import javax.inject.Inject

class QrWalletViewModel @Inject constructor(private val newWalletUseCase: NewWalletUseCase): ViewModel() {

    fun getClipboardWallet(address: String) = newWalletUseCase.getClipboardWallet(address)
}