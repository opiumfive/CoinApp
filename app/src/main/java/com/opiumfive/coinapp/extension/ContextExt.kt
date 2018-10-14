package com.opiumfive.coinapp.extension

import android.content.Context
import android.content.pm.PackageManager
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import org.jetbrains.anko.toast

val Context.screenWidthPx
    get() = resources.displayMetrics.widthPixels

val Context.screenHeightPx
    get() = resources.displayMetrics.heightPixels

fun Context.checkPlayServices(): Boolean {
    val googleApiAvailability = GoogleApiAvailability.getInstance()
    val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this)
    return resultCode == ConnectionResult.SUCCESS
}

fun Context.getColorCompat(@ColorRes colorRes: Int): Int {
    return ContextCompat.getColor(this, colorRes)
}

fun Context.showMessage(message: String?) {
    message?.let { toast(it) }
}

fun Context.isDeviceHaveCamera() = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)