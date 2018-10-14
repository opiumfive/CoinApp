package com.opiumfive.coinapp.extension

import android.support.annotation.StringRes
import com.opiumfive.coinapp.domain.App

fun getString(@StringRes resId: Int) = App.instance.getString(resId) ?: ""
