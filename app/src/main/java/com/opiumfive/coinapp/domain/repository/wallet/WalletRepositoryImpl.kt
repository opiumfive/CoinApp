package com.opiumfive.coinapp.domain.repository.wallet

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import tech.snowfox.betholder.R
import com.opiumfive.coinapp.data.serverModel.apiResponse.ApiResponse
import com.opiumfive.coinapp.data.serverModel.wallet.NewWalletResponse
import com.opiumfive.coinapp.data.bdModel.wallet.PostponedWallet
import com.opiumfive.coinapp.data.uiModel.data.Data
import com.opiumfive.coinapp.data.uiModel.wallet.WalletType
import com.opiumfive.coinapp.domain.AppExecutors
import com.opiumfive.coinapp.domain.Prefs
import com.opiumfive.coinapp.domain.network.ApiService
import com.opiumfive.coinapp.domain.network.mapper.ApiResponseDataMapper
import com.opiumfive.coinapp.domain.throwable.NoInternetThrowable
import com.opiumfive.coinapp.extension.getString
import java.util.*

private const val LIST_WALLETS_LIMIT = 1000

class WalletRepositoryImpl(
    private val apiService: ApiService,
//    private val walletDao: WalletDao,
    private val prefs: Prefs,
    private val appExecutors: AppExecutors
) : IWalletRepository {

    override fun addWallet(
        address: String,
        walletName: String,
        walletType: WalletType
    ): LiveData<Data<NewWalletResponse>> {
        val liveData = MediatorLiveData<Data<NewWalletResponse>>()
        liveData.postValue(Data.loading())
        val source = apiService.newWallet(walletName, address, walletType.name.toLowerCase(Locale.US))
        liveData.addSource(source) {
            handleNoInternetThrowable(it)
            liveData.postValue(ApiResponseDataMapper.map(it))
        }
        return liveData
    }

    override fun addPostponedWallet(postponedWallet: PostponedWallet) {
        prefs.savePostponedWallet(postponedWallet)
        prefs.setPostponedWalletStatus(false)
    }

    override fun uploadPostponedWallet(): LiveData<Data<NewWalletResponse>> {
        val liveData = MediatorLiveData<Data<NewWalletResponse>>()
        if (prefs.isPostponedWalletLoaded()) return liveData.apply { postValue(Data.success(null)) }

        val postponedWallet = prefs.getPostponedWallet()
        if (postponedWallet == null) {
            /**
             * Somehow user don't save postponed wallet in this step
             * Just ignore then
             */
            return liveData.apply { postValue(Data.success(null)) }
        }

        liveData.addSource(addWallet(postponedWallet.address, postponedWallet.walletName, postponedWallet.walletType)) {
            liveData.postValue(it)
            prefs.setPostponedWalletStatus(true)
        }

        return liveData
    }

//    override fun getWallets(): LiveData<Data<List<WalletModel>>> {
//        val liveData = MediatorLiveData<Data<List<WalletModel>>>()
//        liveData.postValue(Data.loading())
//        loadWalletsFromBd(liveData)
//        return liveData
//    }
//
//    private fun loadWalletsFromBd(liveData: MediatorLiveData<Data<List<WalletModel>>>) {
//        val bdSource = walletDao.getWallets()
//        liveData.addSource(bdSource) {
//            it ?: return@addSource
//            liveData.removeSource(bdSource)
//
//            if (it.isNotEmpty()) {
//                val models = it.map { WalletsMapper.map(it) }.asReversed()
//                liveData.postValue(Data.success(models))
//            }
//
//            loadWalletsFromNet(liveData)
//        }
//    }
//
//    private fun loadWalletsFromNet(liveData: MediatorLiveData<Data<List<WalletModel>>>) {
//        liveData.addSource(apiService.getWallets(LIST_WALLETS_LIMIT)) {
//            val walletsResponse = ApiResponseDataMapper.map(it)
//            val walletsList = walletsResponse.body?.asReversed()
//
//            when (walletsList == null) {
//                true -> liveData.postValue(Data.errorThrowable(walletsResponse.throwable))
//                false -> {
//                    val wallets = walletsList?.map { WalletsMapper.map(it) }
//                    liveData.postValue(Data.success(wallets))
//
//                    walletsList?.let {
//                        appExecutors.diskIO().execute {
//                            val bdItems = it.map { WalletsMapper.mapBd(it) }
//                            walletDao.deleteWallets()
//                            walletDao.addWallets(bdItems)
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    override fun getWallet(id: Int): LiveData<Data<WalletModel>> {
//        val liveData = MediatorLiveData<Data<WalletModel>>()
//        loadWalletFromBd(liveData, id)
//        return liveData
//    }
//
//    private fun loadWalletFromBd(liveData: MediatorLiveData<Data<WalletModel>>, id: Int) {
//        val bdSource = walletDao.getWallet(id)
//        liveData.addSource(bdSource) {
//            it ?: return@addSource
//            liveData.removeSource(bdSource)
//
//            liveData.postValue(Data.success(WalletsMapper.map(it)))
//
//            loadWalletFromNet(liveData, id)
//        }
//    }
//
//    private fun loadWalletFromNet(liveData: MediatorLiveData<Data<WalletModel>>, id: Int) {
//        liveData.addSource(apiService.updateWallet(id)) {
//            holdWalletFromNetResponse(liveData, it)
//        }
//    }
//
//    private fun holdWalletFromNetResponse(
//        liveData: MediatorLiveData<Data<WalletModel>>,
//        response: ApiResponse<WalletResponse>?
//    ){
//        val walletsResponse = ApiResponseDataMapper.map(response)
//        val wallet = walletsResponse.body
//
//        when (wallet) {
//            null -> liveData.postValue(Data.errorThrowable(walletsResponse.throwable))
//            else -> {
//                val walletModel = WalletsMapper.map(wallet)
//                liveData.postValue(Data.success(walletModel, walletsResponse.throwable))
//                saveWalletToBd(wallet)
//            }
//        }
//    }
//
//    private fun saveWalletToBd(wallet: WalletResponse){
//        appExecutors.diskIO().execute {
//            val bdWallet = WalletsMapper.mapBd(wallet)
//            walletDao.changeWallet(bdWallet)
//        }
//    }
//
//    override fun changeWalletName(walletId: Int, name: String): LiveData<Data<BaseResponse>> {
//        val liveData = MediatorLiveData<Data<BaseResponse>>()
//        liveData.addSource(apiService.changeWallet(walletId, name)) {
//            handleNoInternetThrowable(it)
//            liveData.postValue(ApiResponseDataMapper.map(it))
//
//            if (it?.isSuccess() == true) {
//                saveWalletToBd(liveData, walletId, name)
//            }
//        }
//        return liveData
//    }
//
//    private fun saveWalletToBd(liveData: MediatorLiveData<Data<BaseResponse>>, walletId: Int, name: String) {
//        val walletSource = walletDao.getWallet(walletId)
//        liveData.addSource(walletSource) {
//            liveData.removeSource(walletSource)
//            it ?: return@addSource
//            it.walletName = name
//            appExecutors.diskIO().execute {
//                walletDao.changeWallet(it)
//            }
//        }
//    }
//
//    override fun switchPushWallet(walletId: Int, check: Boolean): LiveData<Data<WalletModel>> {
//        val liveData = MediatorLiveData<Data<WalletModel>>()
//        val checkNotInt = if (check) 0 else 1
//        liveData.addSource(apiService.switchPush(walletId, checkNotInt)) {
//            holdWalletFromNetResponse(liveData, it)
//        }
//
//        return liveData
//    }
//
//    override fun deleteWallet(walletId: Int): LiveData<Data<BaseResponse>> {
//        val liveData = MediatorLiveData<Data<BaseResponse>>()
//        liveData.addSource(apiService.deleteWallet(walletId)) {
//            handleNoInternetThrowable(it)
//            liveData.postValue(ApiResponseDataMapper.map(it))
//
//            if (it?.isSuccess() == true) {
//                deleteWallet(liveData, walletId)
//            }
//        }
//        return liveData
//    }
//
//    private fun deleteWallet(liveData: MediatorLiveData<Data<BaseResponse>>, walletId: Int) {
//        val walletSource = walletDao.getWallet(walletId)
//        liveData.addSource(walletSource) {
//            liveData.removeSource(walletSource)
//            it ?: return@addSource
//            appExecutors.diskIO().execute {
//                walletDao.deleteWallet(it)
//            }
//        }
//    }

    private fun handleNoInternetThrowable(response: ApiResponse<*>?) {
        if (response?.throwable is NoInternetThrowable) {
            response.throwable = NoInternetThrowable(getString(R.string.error_no_internet_action))
        }
    }
}