package com.opiumfive.coinapp.data.uiModel.data

import com.opiumfive.coinapp.domain.throwable.ThrowableFactory

class Data<out T>(
    val status: Status,
    val body: T? = null,
    var throwable: Throwable? = null,
    val code: Int? = null
) {

    companion object {
        fun loading() = Data(Status.LOADING, null)

        fun <T> loading(cache: T?) = Data(Status.LOADING, cache)

        fun <T> success(body: T?, throwable: Throwable? = null) =
            Data(Status.SUCCESS, body, throwable)

        fun errorThrowable(throwable: Throwable? = null, code: Int? = null): Data<Nothing> {
            return Data(Status.FAILURE, null, throwable, code)
        }

        fun errorMessage(errorMessage: String? = null, code: Int? = null): Data<Nothing> {
            return Data(
                status = Status.FAILURE,
                body = null,
                throwable = ThrowableFactory.getThrowableType(errorMessage),
                code = code
            )
        }
    }

    fun isSuccess() = status == Status.SUCCESS
    fun isLoading() = status == Status.LOADING
    fun isError() = status == Status.FAILURE
}