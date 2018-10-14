package com.opiumfive.coinapp.domain.di.component

import dagger.Component
import com.opiumfive.coinapp.domain.di.module.AppModule
import com.opiumfive.coinapp.ui.feature.auth.activity.AuthActivity
import com.opiumfive.coinapp.ui.feature.main.activity.MainActivity
import com.opiumfive.coinapp.ui.feature.main.fragment.BalanceFragment
import com.opiumfive.coinapp.ui.feature.main.model.TestFragment
import com.opiumfive.coinapp.ui.feature.newWallet.activity.QrWalletAddressActivity
import com.opiumfive.coinapp.ui.feature.newWallet.fragment.NewWalletDialogFragment
import com.opiumfive.coinapp.ui.feature.onboarding.activity.OnboardingActivity
import com.opiumfive.coinapp.ui.feature.setting.fragment.SettingFragment
import com.opiumfive.coinapp.ui.feature.transactions.fragment.TransactionsFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    //Activities
    fun inject(activity: OnboardingActivity)
    fun inject(activity: AuthActivity)
    fun inject(activity: MainActivity)
    fun inject(activity: QrWalletAddressActivity)

    //Fragments
    fun inject(testFragment: TestFragment)
    fun inject(balanceFragment: BalanceFragment)
    fun inject(transactionsFragment: TransactionsFragment)
    fun inject(settingFragment: SettingFragment)
    fun inject(newWalletFragment: NewWalletDialogFragment)
}