package com.opiumfive.coinapp.data.bdModel.main

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "main_balance")
data class MainBalanceBdModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val totalBalance: String,
    val income: String,
    val currency: String,
    val btcBalance: String,
    val ethBalance: String,
    val tokenBalance: String
)
