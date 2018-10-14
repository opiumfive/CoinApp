package com.opiumfive.coinapp.domain.network.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import com.opiumfive.coinapp.domain.Prefs

class AuthInterceptor(private val prefs: Prefs) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = addHeaders(chain.request())
        val response = chain.proceed(builder.build())
        if (response.code() == 401 && response.request().url().toString().contains("onesignal").not()) {
            //TODO replace with actual activity
            //LoginActivity.startActivityAsTop(App.instance, true)
        }
        return response
    }

    private fun addHeaders(request: Request): Request.Builder {
        val builder = request.newBuilder()

        prefs.getToken()?.let {
            builder.header("Authorization", it)
        }

        return builder
    }
}