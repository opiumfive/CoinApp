package com.opiumfive.coinapp.domain.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import com.opiumfive.coinapp.domain.di.factory.ViewModelFactory
import com.opiumfive.coinapp.domain.di.factory.ViewModelKey
import com.opiumfive.coinapp.ui.feature.auth.model.AuthViewModel
import com.opiumfive.coinapp.ui.feature.main.model.MainViewModel
import com.opiumfive.coinapp.ui.feature.onboarding.model.OnboardingViewModel
import com.opiumfive.coinapp.ui.feature.setting.model.SettingsViewModel
import com.opiumfive.coinapp.ui.feature.transactions.model.TransactionViewModel
import com.opiumfive.coinapp.ui.feature.newWallet.model.NewWalletViewModel
import com.opiumfive.coinapp.ui.feature.newWallet.model.QrWalletViewModel

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(OnboardingViewModel::class)
    internal abstract fun onboardingViewModel(viewModel: OnboardingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    internal abstract fun authViewModel(viewModel: AuthViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun mainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TransactionViewModel::class)
    internal abstract fun transactionViewModel(transactionViewModel: TransactionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    internal abstract fun settingsViewModel(settingsViewModel: SettingsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewWalletViewModel::class)
    internal abstract fun newWalletViewModel(newWalletViewModel: NewWalletViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(QrWalletViewModel::class)
    internal abstract fun qrWalletViewModel(qrWalletViewModel: QrWalletViewModel): ViewModel
}