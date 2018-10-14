package com.opiumfive.coinapp.domain.bd

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.opiumfive.coinapp.data.bdModel.main.MainBalanceBdModel
import com.opiumfive.coinapp.data.bdModel.transactions.TransactionBdModel
import com.opiumfive.coinapp.domain.bd.dao.MainBalanceDao
import com.opiumfive.coinapp.domain.bd.dao.TransactionsDao

private const val VERSION = 1

@Database(entities = [(MainBalanceBdModel::class), (TransactionBdModel::class)], version = VERSION, exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract fun mainBalanceDao(): MainBalanceDao
    abstract fun transactionsDao(): TransactionsDao
}