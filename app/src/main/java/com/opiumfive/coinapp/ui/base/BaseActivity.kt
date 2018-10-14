package com.opiumfive.coinapp.ui.base

import android.arch.lifecycle.ViewModel
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v7.app.AppCompatActivity
import com.opiumfive.coinapp.domain.App
import com.opiumfive.coinapp.domain.di.component.AppComponent
import com.opiumfive.coinapp.domain.di.factory.ViewModelFactory
import com.opiumfive.coinapp.extension.getViewModel
import javax.inject.Inject

abstract class BaseActivity: AppCompatActivity() {

    @Inject protected lateinit var modelFactory: ViewModelFactory

    @CallSuper override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDI((application as App).appComponent)
    }

    abstract fun injectDI(appComponent: AppComponent)

    protected inline fun <reified T : ViewModel> initViewModel() = getViewModel<T>(modelFactory)
}