package com.opiumfive.coinapp.domain

import android.app.Application
import android.support.v7.app.AppCompatDelegate
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import com.opiumfive.coinapp.domain.di.component.DaggerAppComponent
import com.opiumfive.coinapp.domain.di.module.AppModule

class App: Application() {

    companion object {
        lateinit var instance: App
            private set
    }

    val appComponent by lazy {
        DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        Fabric.with(this, Crashlytics())
    }
}