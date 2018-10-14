package com.opiumfive.coinapp.ui.feature.main.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import android.os.Handler
import com.opiumfive.coinapp.data.uiModel.data.Data
import com.opiumfive.coinapp.data.uiModel.main.MainBalanceModel
import com.opiumfive.coinapp.domain.throwable.NoInternetThrowable
import com.opiumfive.coinapp.domain.useCase.MainBalanceUseCase
import javax.inject.Inject

private const val UPDATE_TIME = 15 * 1000L

class MainViewModel @Inject constructor(private val mainBalanceUseCase: MainBalanceUseCase) : ViewModel() {

    private val mainBalance = MediatorLiveData<Data<MainBalanceModel>>()
    private var previousMainBalance: LiveData<Data<MainBalanceModel>>? = null

    private var previousErrorTime = 0L
    private var noInternetLiveData = MediatorLiveData<NoInternetThrowable>()

    private val updateTask = Runnable {
        getMainBalance()
        updateTimer()
    }

    private val updateHandler = Handler()

    private var updateTime = UPDATE_TIME


    fun getMainBalance(): LiveData<Data<MainBalanceModel>> {
        previousMainBalance?.let { mainBalance.removeSource(it) }
        previousMainBalance = mainBalanceUseCase.loadMainBalance()
        previousMainBalance?.let { previousSource ->
            mainBalance.addSource(previousSource) {
                handleThrowable(it)
                val previousValue = mainBalance.value
                when {
                    previousValue != null && previousValue.isSuccess() && it?.isLoading() == true -> {
                        mainBalance.postValue(previousValue)
                    }

                    else -> mainBalance.postValue(it)
                }
            }
        }

        return mainBalance
    }

    fun startUpdate() {
        updateHandler.postDelayed(updateTask, updateTime)
    }

    fun stopUpdate() {
        updateHandler.removeCallbacks(updateTask)
    }

    private fun updateTimer() {
        updateHandler.removeCallbacks(updateTask)
        updateHandler.postDelayed(updateTask, updateTime)
    }

    private fun handleThrowable(data: Data<*>?) {
        val dataThrowable = data?.throwable ?: return
        if (dataThrowable is NoInternetThrowable && previousErrorTime + UPDATE_TIME <= System.currentTimeMillis()) {
            noInternetLiveData.postValue(dataThrowable)
            previousErrorTime = System.currentTimeMillis()
        }
    }

    fun noInternetHandler() = noInternetLiveData
}