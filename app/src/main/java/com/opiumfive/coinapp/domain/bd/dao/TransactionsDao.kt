package com.opiumfive.coinapp.domain.bd.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import com.opiumfive.coinapp.data.bdModel.transactions.TransactionBdModel

@Dao
interface TransactionsDao {

    @Query("select * from transactions WHERE walletId = :walletId AND timestamp <= :timestamp ORDER BY timestamp DESC LIMIT :limit")
    fun getTransactions(walletId: Int, timestamp: Long, limit: Int): LiveData<List<TransactionBdModel>>

    @Insert(onConflict = REPLACE)
    fun addTransactions(transactions: List<TransactionBdModel>)

    @Query("DELETE FROM transactions")
    fun deleteTransactions()
}