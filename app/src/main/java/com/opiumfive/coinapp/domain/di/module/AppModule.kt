package com.opiumfive.coinapp.domain.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import com.opiumfive.coinapp.domain.AppExecutors
import com.opiumfive.coinapp.domain.Prefs
import com.opiumfive.coinapp.domain.bd.DatabaseBuilder
import com.opiumfive.coinapp.domain.network.ApiBuilder
import javax.inject.Singleton

@Module(includes = [DaoModule::class, RepositoryModule::class, UseCaseModule::class, ViewModelModule::class])
class AppModule(private val context: Context) {
    @Provides
    fun provideContext() = context

    @Provides
    @Singleton
    fun provideApi(context: Context, prefs: Prefs) = ApiBuilder.build(context, prefs)

    @Provides
    @Singleton
    fun provideGoogleApi() = ApiBuilder.buildGoogleService()

    @Provides
    @Singleton
    fun provideDatabase(context: Context) = DatabaseBuilder.build(context)

    @Provides
    @Singleton
    fun provideExecutors() = AppExecutors()
}