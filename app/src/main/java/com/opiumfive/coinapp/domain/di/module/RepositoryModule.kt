package com.opiumfive.coinapp.domain.di.module

import dagger.Module
import dagger.Provides
import com.opiumfive.coinapp.domain.AppExecutors
import com.opiumfive.coinapp.domain.Prefs
import com.opiumfive.coinapp.domain.bd.dao.MainBalanceDao
import com.opiumfive.coinapp.domain.bd.dao.TransactionsDao
import com.opiumfive.coinapp.domain.network.ApiService
import com.opiumfive.coinapp.domain.repository.main.IMainBalanceRepository
import com.opiumfive.coinapp.domain.repository.main.MainBalanceRepositoryImpl
import com.opiumfive.coinapp.domain.repository.transaction.ITransactionRepository
import com.opiumfive.coinapp.domain.repository.transaction.TransactionRepositoryImpl
import com.opiumfive.coinapp.domain.repository.wallet.IWalletRepository
import com.opiumfive.coinapp.domain.repository.wallet.WalletRepositoryImpl
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideMainBalanceRepository(
        apiService: ApiService,
        mainBalanceDao: MainBalanceDao,
        appExecutors: AppExecutors
    ) = MainBalanceRepositoryImpl(apiService, mainBalanceDao, appExecutors) as IMainBalanceRepository

    @Provides
    @Singleton
    fun provideTransactionRepository(
        apiService: ApiService,
        transactionsDao: TransactionsDao,
        appExecutors: AppExecutors
    ) = TransactionRepositoryImpl(apiService, transactionsDao, appExecutors) as ITransactionRepository

    @Provides
    @Singleton
    fun provideWalletsRepository(
        apiService: ApiService,
        prefs: Prefs,
        appExecutors: AppExecutors
    ) = WalletRepositoryImpl(apiService, prefs, appExecutors) as IWalletRepository
}