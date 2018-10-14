package com.opiumfive.coinapp.domain.di.module

import dagger.Module
import dagger.Provides
import com.opiumfive.coinapp.domain.bd.Database

@Module
class DaoModule {

    @Provides
    fun provideMainBalanceDao(database: Database) = database.mainBalanceDao()

    @Provides
    fun provideTransactionsDao(database: Database) = database.transactionsDao()
}