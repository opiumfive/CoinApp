package com.opiumfive.coinapp.domain.throwable

object ThrowableFactory {

    fun getThrowableType(throwable: Throwable): Throwable {
        return getThrowable(throwable.message)
    }

    fun getThrowableType(message: String?): Throwable {
        return getThrowable(message)
    }

    private fun getThrowable(message: String?): Throwable {
        message ?: return Throwable()
        return when {
            else -> Throwable(message)
        }
    }
}