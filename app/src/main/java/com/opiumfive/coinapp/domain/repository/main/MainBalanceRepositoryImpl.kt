package com.opiumfive.coinapp.domain.repository.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.opiumfive.coinapp.data.uiModel.data.Data
import com.opiumfive.coinapp.data.uiModel.main.MainBalanceModel
import com.opiumfive.coinapp.domain.bd.dao.MainBalanceDao
import com.opiumfive.coinapp.domain.AppExecutors
import com.opiumfive.coinapp.domain.mapper.MainBalanceMapper
import com.opiumfive.coinapp.domain.network.ApiService
import com.opiumfive.coinapp.domain.network.mapper.ApiResponseDataMapper

class MainBalanceRepositoryImpl(
        private val apiService: ApiService,
        private val mainBalanceDao: MainBalanceDao,
        private val appExecutors: AppExecutors
) : IMainBalanceRepository {

    override fun getMainBalance(): LiveData<Data<MainBalanceModel>> {
        val liveData = MediatorLiveData<Data<MainBalanceModel>>()
        liveData.postValue(Data.loading())
        loadMainBalanceFromBdAndLoadFromNet(liveData)
        return liveData
    }

    private fun loadMainBalanceFromBdAndLoadFromNet(liveData: MediatorLiveData<Data<MainBalanceModel>>) {
        val bdSource = mainBalanceDao.getMainBalance()
        liveData.addSource(bdSource) {

            liveData.removeSource(bdSource)

            it?.let {
                val model = MainBalanceMapper.map(it)
                liveData.postValue(Data.success(model))
            }

            loadMainBalanceFromNet(liveData)
        }
    }

    private fun loadMainBalanceFromNet(liveData: MediatorLiveData<Data<MainBalanceModel>>) {
        liveData.addSource(apiService.getMainBalance()) {
            val mainBalanceResponse = ApiResponseDataMapper.map(it)
            val mainBalance = mainBalanceResponse.body

            if (mainBalance != null) {
                val model = MainBalanceMapper.map(mainBalance)
                liveData.postValue(Data.success(model))

                appExecutors.diskIO().execute {
                    val bdItem = MainBalanceMapper.mapBd(mainBalance)
                    mainBalanceDao.deleteBalance()
                    mainBalanceDao.addMainBalance(bdItem)
                }
            } else {
                liveData.postValue(Data.errorThrowable(mainBalanceResponse.throwable))
            }
        }
    }
}