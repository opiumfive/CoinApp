package com.opiumfive.coinapp.domain.repository.transaction

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.opiumfive.coinapp.data.serverModel.transactions.TransactionsResponse
import com.opiumfive.coinapp.data.uiModel.data.Data
import com.opiumfive.coinapp.data.uiModel.transactions.TransactionsModel
import com.opiumfive.coinapp.domain.AppExecutors
import com.opiumfive.coinapp.domain.bd.dao.TransactionsDao
import com.opiumfive.coinapp.domain.mapper.TransactionMapper
import com.opiumfive.coinapp.domain.network.ApiService
import com.opiumfive.coinapp.domain.throwable.InvalidDataThrowable
import java.util.*
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
        private val apiService: ApiService,
        private val transactionsDao: TransactionsDao,
        private val appExecutors: AppExecutors
) : ITransactionRepository {

    override fun getTransactions(walletId: Int, start: Long, limit: Int): LiveData<Data<List<TransactionsModel>>> {
        val liveData = MediatorLiveData<Data<List<TransactionsModel>>>()
        liveData.postValue(Data.loading())
        loadFromBd(walletId, start, limit, liveData)
        return liveData
    }

    private fun loadFromBd(walletId: Int, start: Long, limit: Int, liveData: MediatorLiveData<Data<List<TransactionsModel>>>) {
        val from = if (start == 0L) Calendar.getInstance().timeInMillis / 1000 else start

        val bdSource = transactionsDao.getTransactions(walletId, from, limit)
        liveData.addSource(bdSource) { source ->
            liveData.removeSource(bdSource)
            val items = source?.map { TransactionMapper.map(it) }
            if (items != null && items.isNotEmpty()) {
                liveData.postValue(Data.success(items))
            }

            loadFromNet(walletId, start, limit, liveData, items)
        }
    }

    private fun loadFromNet(
            walletId: Int,
            start: Long,
            perPage: Int,
            liveData: MediatorLiveData<Data<List<TransactionsModel>>>,
            bdItems: List<TransactionsModel>?
    ) {
        if (start > 0 && bdItems?.size == perPage) return

        val from = if (start == 0L) null else start
        val serverSource = apiService.getTransactions(walletId, start, perPage)
        liveData.addSource(serverSource) {
            liveData.removeSource(serverSource)

            val items = it?.body
            when (items == null) {
                true -> liveData.postValue(Data.errorThrowable(it?.throwable))

                else -> handleApiResult(items, bdItems, from, liveData, walletId)
            }
        }
    }

    private fun handleApiResult(
            items: List<TransactionsResponse>?,
            bdItems: List<TransactionsModel>?,
            from: Long?,
            liveData: MediatorLiveData<Data<List<TransactionsModel>>>,
            walletId: Int
    ) {
        val apiTemp = items?.map { TransactionMapper.map(it, from) }

        val firstItem = if (bdItems?.size ?: 0 > 0) bdItems?.get(0) else null
        val bdContainsApiResponse = apiTemp?.any { it.timestamp == firstItem?.timestamp } == true

        val needToClearBd = from == null &&
                bdItems?.isEmpty()?.not() == true &&
                bdContainsApiResponse.not()

        if (needToClearBd.not()) {
            val list = mutableListOf<TransactionsModel>()
            apiTemp?.let { list.addAll(it) }
            bdItems?.forEach { bdItem ->
                val needToAdd = list.any { it.id == bdItem.id }.not()
                if (needToAdd) list.add(bdItem)
            }

            liveData.postValue(Data.success(list))
        }

        val bdTemp = items?.map { TransactionMapper.mapBd(it, from) }
        bdTemp?.let {
            appExecutors.diskIO().execute {
                if (needToClearBd) {
                    transactionsDao.deleteTransactions()
                    // Pass throwable to clear previous data
                    liveData.postValue(Data.errorThrowable(InvalidDataThrowable()))
                    liveData.postValue(Data.success(apiTemp))
                }

                transactionsDao.addTransactions(it)
            }
        }
    }
}