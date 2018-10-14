package com.opiumfive.coinapp.ui.feature.main.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_balance.*
import tech.snowfox.betholder.R
import com.opiumfive.coinapp.data.uiModel.data.Data
import com.opiumfive.coinapp.data.uiModel.main.MainBalanceModel
import com.opiumfive.coinapp.domain.di.component.AppComponent
import com.opiumfive.coinapp.domain.throwable.NoInternetThrowable
import com.opiumfive.coinapp.ui.base.BaseFragment
import com.opiumfive.coinapp.ui.feature.main.model.MainViewModel

class BalanceFragment: BaseFragment() {

    private lateinit var mainViewModel: MainViewModel
    private val mainBalanceObserver by lazy { Observer<Data<MainBalanceModel>> { setUpBalanceUI(it?.body) } }
    private val errorObserver by lazy { Observer<NoInternetThrowable> {  } }

    override fun injectDI(appComponent: AppComponent) = appComponent.inject(this)

    companion object {
        fun newInstance() = BalanceFragment()
    }

    private fun setUpBalanceUI(mainModel: MainBalanceModel?) {
        income.text = mainModel?.income
        currency.text = mainModel?.currency
        balance.text = mainModel?.totalBalance
        tokensBalance.text = mainModel?.tokenBalance
        ethBalance.text = mainModel?.ethBalance
        btcBalance.text = mainModel?.btcBalance
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = initViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_balance, container, false)
    }

    override fun onStart() {
        super.onStart()
        mainViewModel.noInternetHandler().observe(this, errorObserver)
        mainViewModel.getMainBalance().observe(this, mainBalanceObserver)
        mainViewModel.startUpdate()
    }

    override fun onStop() {
        super.onStop()
        mainViewModel.stopUpdate()
    }
}