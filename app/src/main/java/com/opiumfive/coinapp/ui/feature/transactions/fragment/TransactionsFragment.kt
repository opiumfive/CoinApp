package com.opiumfive.coinapp.ui.feature.transactions.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tech.snowfox.betholder.R
import com.opiumfive.coinapp.data.uiModel.data.Data
import com.opiumfive.coinapp.data.uiModel.transactions.TransactionsModel
import com.opiumfive.coinapp.domain.di.component.AppComponent
import com.opiumfive.coinapp.ui.base.BaseFragment
import com.opiumfive.coinapp.ui.feature.transactions.model.TransactionViewModel

private const val TRANSACTIONS_PER_PAGE = 40

class TransactionsFragment: BaseFragment() {

    private val viewModel by lazy { initViewModel<TransactionViewModel>(true) }
    private var walletId: Int = 0

    private val firstTransactionsObserver by lazy {
        Observer<Data<List<TransactionsModel>>> { handleFirstTransactions(it) }
    }

    private val moreTransactionsObserver by lazy {
        Observer<Data<List<TransactionsModel>>> { handleMoreTransactions(it) }
    }

    override fun injectDI(appComponent: AppComponent) = appComponent.inject(this)

    companion object {
        fun newInstance() = TransactionsFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_transactions, container, false)
    }

    override fun onStart() {
        super.onStart()
        viewModel.getTransactions(walletId, 0, TRANSACTIONS_PER_PAGE)
                .observe(this, firstTransactionsObserver)
        viewModel.loadForceTransactions(walletId)
    }

    override fun onStop() {
        super.onStop()
        viewModel.stopForceUpdate()
    }

    private fun handleFirstTransactions(data: Data<List<TransactionsModel>>?) {
        data ?: return
    }

    private fun handleMoreTransactions(data: Data<List<TransactionsModel>>?) {

    }
}