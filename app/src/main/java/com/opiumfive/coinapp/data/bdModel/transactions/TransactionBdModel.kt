package com.opiumfive.coinapp.data.bdModel.transactions

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionBdModel(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    var timestamp: Long = 0,
    val walletId: Int,
    val address: String,
    val amount: Double,
    val fees: Double,
    val transactionDate: String,
    val direction: String,
    val confirmations: Int,
    val walletType: String
)