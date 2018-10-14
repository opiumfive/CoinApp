package com.opiumfive.coinapp.domain.repository.transaction

import android.arch.lifecycle.LiveData
import com.opiumfive.coinapp.data.uiModel.data.Data
import com.opiumfive.coinapp.data.uiModel.transactions.TransactionsModel

interface ITransactionRepository {

    fun getTransactions(walletId: Int, timestamp: Long, perPage: Int): LiveData<Data<List<TransactionsModel>>>
}