package com.opiumfive.coinapp.domain.repository.main

import android.arch.lifecycle.LiveData
import com.opiumfive.coinapp.data.uiModel.data.Data
import com.opiumfive.coinapp.data.uiModel.main.MainBalanceModel

interface IMainBalanceRepository {
    fun getMainBalance(): LiveData<Data<MainBalanceModel>>
}