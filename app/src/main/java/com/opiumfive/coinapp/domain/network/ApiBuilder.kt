package com.opiumfive.coinapp.domain.network

import android.content.Context
import com.google.gson.GsonBuilder
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tech.snowfox.betholder.BuildConfig
import com.opiumfive.coinapp.domain.Prefs
import com.opiumfive.coinapp.domain.network.factory.LiveDataCallAdapterFactory
import com.opiumfive.coinapp.domain.network.interceptor.AuthInterceptor
import java.util.concurrent.TimeUnit

private const val BASE_URL_DEV = "http://api.betholder.io/v1/"
private const val TIMEOUT_REQUEST = 30 * 1000L

private const val BASE_URL_GOOGLE_AUTH = "https://www.googleapis.com/"

object ApiBuilder {

    fun build(context: Context, prefs: Prefs)
        = createRetrofit(createOkHttpClient(context, prefs), BASE_URL_DEV)
            .create(ApiService::class.java) as ApiService

    fun buildGoogleService(): GoogleApiService {
        val client = createOkHttpClient()
        return createRetrofit(client, BASE_URL_GOOGLE_AUTH)
            .create(GoogleApiService::class.java)
    }

    private fun createRetrofit(client: OkHttpClient, baseUrl: String)
        = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(createGson()))
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()

    private fun createOkHttpClient(context: Context? = null, prefs: Prefs? = null): OkHttpClient {
        val builder = OkHttpClient().newBuilder()
        if (BuildConfig.DEBUG && context != null) builder.addInterceptor(ChuckInterceptor(context))
        if (prefs != null) builder.addInterceptor(AuthInterceptor(prefs))
        return builder.connectTimeout(TIMEOUT_REQUEST, TimeUnit.MILLISECONDS)
            .readTimeout(TIMEOUT_REQUEST, TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    private fun createGson() = GsonBuilder().create()
}