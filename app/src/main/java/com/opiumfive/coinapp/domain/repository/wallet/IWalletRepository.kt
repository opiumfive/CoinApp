package com.opiumfive.coinapp.domain.repository.wallet

import android.arch.lifecycle.LiveData
import com.opiumfive.coinapp.data.serverModel.wallet.NewWalletResponse
import com.opiumfive.coinapp.data.bdModel.wallet.PostponedWallet
import com.opiumfive.coinapp.data.uiModel.data.Data
import com.opiumfive.coinapp.data.uiModel.wallet.WalletType

interface IWalletRepository {

    fun addWallet(address: String, walletName: String, walletType: WalletType): LiveData<Data<NewWalletResponse>>

    fun addPostponedWallet(postponedWallet: PostponedWallet)

    /**
     * User must be registered
     */
    fun uploadPostponedWallet(): LiveData<Data<NewWalletResponse>>

//    fun getWallets(): LiveData<Data<List<WalletModel>>>
//
//    fun getWallet(id: Int): LiveData<Data<WalletModel>>
//
//    fun changeWalletName(walletId: Int, name: String): LiveData<Data<BaseResponse>>
//
//    fun switchPushWallet(walletId: Int, check: Boolean): LiveData<Data<WalletModel>>
//
//    fun deleteWallet(walletId: Int): LiveData<Data<BaseResponse>>
}