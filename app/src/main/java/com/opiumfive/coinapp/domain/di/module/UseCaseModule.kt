package com.opiumfive.coinapp.domain.di.module

import dagger.Module
import dagger.Provides
import com.opiumfive.coinapp.domain.repository.main.IMainBalanceRepository
import com.opiumfive.coinapp.domain.useCase.MainBalanceUseCase

@Module
class UseCaseModule {
    @Provides
    fun provideMainBalanceUseCase(mainBalanceRepository: IMainBalanceRepository) = MainBalanceUseCase(mainBalanceRepository)
}