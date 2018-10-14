package com.opiumfive.coinapp.data.serverModel.apiResponse

import retrofit2.Response
import retrofit2.Retrofit
import com.opiumfive.coinapp.domain.network.utils.ErrorUtils
import com.opiumfive.coinapp.domain.throwable.ThrowableFactory

class ApiResponse<T>(val body: T? = null, var throwable: Throwable? = null, val code: Int? = null) {
    companion object {
        fun <T> create(throwable: Throwable): ApiResponse<T> {
            val factoryThrowable = ThrowableFactory.getThrowableType(throwable)
            return ApiResponse(throwable = factoryThrowable, code = 500)
        }

        fun <T> create(retrofit: Retrofit, response: Response<T>?): ApiResponse<T> {
            if (response?.isSuccessful == true) {
                return ApiResponse(response.body(), code = 200, throwable = null)
            }

            val error = ErrorUtils.parseError(retrofit, response)
            val throwable = ThrowableFactory.getThrowableType(error.error)

            return ApiResponse(throwable = throwable, code = response?.code())
        }
    }

    fun isSuccess() = body != null
}