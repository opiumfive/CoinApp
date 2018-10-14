package com.opiumfive.coinapp.domain.network.adapter

import android.arch.lifecycle.LiveData
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import com.opiumfive.coinapp.data.serverModel.apiResponse.ApiResponse
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

class LiveDataCallAdapter<T>(
    private val retrofit: Retrofit,
    private val responseType: Type
) : CallAdapter<T, LiveData<ApiResponse<T>>> {

    override fun responseType(): Type {
        return responseType
    }

    override fun adapt(call: Call<T>): LiveData<ApiResponse<T>> {
        return object : LiveData<ApiResponse<T>>(), Callback<T> {
            private var started = AtomicBoolean(false)

            override fun onActive() {
                super.onActive()
                if (started.compareAndSet(false, true)) {
                    call.enqueue(this)
                }
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                postValue(ApiResponse.create(retrofit, response))
            }

            override fun onFailure(call: Call<T>, throwable: Throwable) {
                postValue(ApiResponse.create(throwable))
            }
        }
    }
}
