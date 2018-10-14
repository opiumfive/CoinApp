package com.opiumfive.coinapp.domain.bd.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.opiumfive.coinapp.data.bdModel.main.MainBalanceBdModel

@Dao
interface MainBalanceDao {

    @Query("select * from main_balance ORDER BY id DESC LIMIT 1")
    fun getMainBalance(): LiveData<MainBalanceBdModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMainBalance(mainBalance: MainBalanceBdModel)

    @Query("DELETE FROM main_balance")
    fun deleteBalance()
}