package com.opiumfive.coinapp.extension

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

inline fun <reified T : ViewModel> FragmentActivity.getViewModel(viewModelFactory: ViewModelProvider.Factory): T {
    return ViewModelProviders.of(this, viewModelFactory)[T::class.java]
}

inline fun <reified T : ViewModel> Fragment.getViewModel(viewModelFactory: ViewModelProvider.Factory, fromActivity: Boolean = false): T {
    return when (fromActivity) {
        false -> ViewModelProviders.of(this, viewModelFactory)[T::class.java]
        else -> ViewModelProviders.of(activity!!, viewModelFactory)[T::class.java]
    }
}