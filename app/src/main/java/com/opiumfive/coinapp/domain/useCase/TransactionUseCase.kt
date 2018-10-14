package com.opiumfive.coinapp.domain.useCase

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.opiumfive.coinapp.data.uiModel.data.Data
import com.opiumfive.coinapp.data.uiModel.data.Status
import com.opiumfive.coinapp.data.uiModel.transactions.TransactionsModel
import com.opiumfive.coinapp.domain.Prefs
import com.opiumfive.coinapp.domain.network.ApiService
import com.opiumfive.coinapp.domain.repository.transaction.ITransactionRepository
import javax.inject.Inject

class TransactionUseCase
@Inject constructor(
        private val apiService: ApiService,
        private val repository: ITransactionRepository,
        private val prefs: Prefs
) {

    fun getTransactions(walletId: Int, start: Long, limit: Int): LiveData<Data<List<TransactionsModel>>> {

        val liveData = MediatorLiveData<Data<List<TransactionsModel>>>()
        val source = repository.getTransactions(walletId, start, limit)
        liveData.addSource(source) { repositoryData ->
            repositoryData ?: return@addSource
            when (repositoryData.status) {
                Status.LOADING,
                Status.FAILURE -> liveData.postValue(repositoryData)
                Status.SUCCESS -> {
                    val body = repositoryData.body
                    setNewTransactions(walletId, body)
                    liveData.postValue(Data.success(body))
                }
            }
        }

        return liveData
    }

    fun setNewTransactions (walletId: Int, items: List<TransactionsModel>?) {
        val lastTimestamp = prefs.getLastTransactionTimestamp(walletId)
        //items?.filter { it.walletId == walletId }?.forEach { it.new = it.timestamp > lastTimestamp && lastTimestamp != 0L }
    }

    fun saveLastTransactionTimestamp(walletId: Int, timestamp: Long) {
        prefs.saveLastTransactionTimestamp(walletId, timestamp)
    }
}