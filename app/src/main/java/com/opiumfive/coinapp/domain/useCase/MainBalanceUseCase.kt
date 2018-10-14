package com.opiumfive.coinapp.domain.useCase

import android.arch.lifecycle.LiveData
import com.opiumfive.coinapp.data.uiModel.data.Data
import com.opiumfive.coinapp.data.uiModel.main.MainBalanceModel
import com.opiumfive.coinapp.domain.repository.main.IMainBalanceRepository
import javax.inject.Inject

class MainBalanceUseCase @Inject constructor(private var repository: IMainBalanceRepository) {

    fun loadMainBalance(): LiveData<Data<MainBalanceModel>> = repository.getMainBalance()

}