package com.opiumfive.coinapp.ui.base

import android.arch.lifecycle.ViewModel
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.opiumfive.coinapp.domain.App
import com.opiumfive.coinapp.domain.di.component.AppComponent
import com.opiumfive.coinapp.domain.di.factory.ViewModelFactory
import com.opiumfive.coinapp.extension.getViewModel

import javax.inject.Inject

abstract class BaseFragment : Fragment() {

    @Inject protected lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDI((activity?.application as App).appComponent)
    }

    abstract fun injectDI(appComponent: AppComponent)

    protected inline fun <reified T : ViewModel> initViewModel(fromActivity: Boolean = false) = getViewModel<T>(viewModelFactory, fromActivity)

    override fun getContext(): Context {
        return super.getContext() ?: throw IllegalArgumentException("Context must not be null")
    }

    val activityNotNull: AppCompatActivity
        get() = activity as? AppCompatActivity
            ?: throw IllegalArgumentException("Activity must not be null")

}