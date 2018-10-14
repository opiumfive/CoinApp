package com.opiumfive.coinapp.ui.feature.transactions.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import android.os.Handler
import com.opiumfive.coinapp.data.uiModel.data.Data
import com.opiumfive.coinapp.data.uiModel.data.Status
import com.opiumfive.coinapp.data.uiModel.transactions.TransactionsModel
import com.opiumfive.coinapp.domain.throwable.InvalidDataThrowable
import com.opiumfive.coinapp.domain.useCase.TransactionUseCase
import javax.inject.Inject

private const val UPDATE_TIME_FORCE = 15 * 1000L

class TransactionViewModel
@Inject constructor(private val transactionUseCase: TransactionUseCase) : ViewModel() {

    private var limit = 0

    private var currentWalletId: Int = 0
    private val liveData = MediatorLiveData<Data<List<TransactionsModel>>>()
    private val tempData = mutableListOf<TransactionsModel>()
    private var previousSource: LiveData<Data<List<TransactionsModel>>>? = null

    private val forceUpdateHandler = Handler()
    private val forceUpdateTask = Runnable {
        if (currentWalletId != 0){
            loadForceTransactions(currentWalletId)
            updateTimer()
        }
    }
    private var forceUpdateSource: LiveData<Data<List<TransactionsModel>>>? = null
    private var forceUpdating = false

    fun getTransactions(walletId: Int, start: Long, limit: Int): LiveData<Data<List<TransactionsModel>>> {
        this.limit = limit
        if (walletId == currentWalletId) return liveData
        if (walletId != currentWalletId) {
            previousSource?.let { liveData.removeSource(it) }
            currentWalletId = walletId
            tempData.clear()
        }
        loadMoreTransactions(walletId, start, limit)
        return liveData
    }

    fun setNewTransactions(walletId: Int, items: List<TransactionsModel>?) {
        transactionUseCase.setNewTransactions(walletId, items)
    }

    fun loadForceTransactions(walletId: Int){
        if (walletId != currentWalletId) {
            forceUpdating = false
            return
        }
        if (forceUpdating) return
        forceUpdating = true
        forceUpdateSource?.let { liveData.removeSource(it) }
        forceUpdateSource = transactionUseCase.getTransactions(currentWalletId, 0, limit)
        forceUpdateSource?.let { source ->
            liveData.addSource(source) { data ->
                forceUpdating = false
                handleResult(data)
                updateTimer()
            }
        }
    }

    fun stopForceUpdate() {
        forceUpdateHandler.removeCallbacks(forceUpdateTask)
    }

    private fun updateTimer() {
        forceUpdateHandler.removeCallbacks(forceUpdateTask)
        forceUpdateHandler.postDelayed(forceUpdateTask, UPDATE_TIME_FORCE)
    }

    fun loadMoreTransactions(walletId: Int, start: Long, limit: Int): LiveData<Data<List<TransactionsModel>>> {
        this.limit = limit
        if (walletId != currentWalletId) return liveData
        previousSource = transactionUseCase.getTransactions(walletId, start, limit)
        previousSource?.let { liveData.addSource(it) { handleResult(it) } }

        return liveData
    }

    private fun handleResult(data: Data<List<TransactionsModel>>?){
        when (data?.status) {
            /** We dont't want to show loading if temp is not empty */
            Status.LOADING -> {
                if (tempData.isEmpty()) liveData.postValue(data)
            }

            Status.SUCCESS -> {
                val tempList = mutableListOf<TransactionsModel>()
                data.body?.let {
                    tempList.addAll(it)
                }

                //tempData.mergeSortedCollections(tempList, transactionComparator)

                liveData.postValue(Data.success(tempData.toList()))
            }
            Status.FAILURE -> {
                if (data.throwable is InvalidDataThrowable){
                    tempData.clear()
                } else {
                    liveData.postValue(data)
                }
            }
        }
    }

    fun saveLastTransactionTimestamp(walletId: Int, timestamp: Long){
        transactionUseCase.saveLastTransactionTimestamp(walletId, timestamp)
    }
}