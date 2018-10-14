package com.opiumfive.coinapp.domain

import android.annotation.SuppressLint
import android.os.Build
import android.provider.Settings
import tech.snowfox.betholder.BuildConfig
import java.util.*

object DomainUtils {

    @SuppressLint("HardwareIds")
    fun getDeviceId(): String {
        val contentResolver = App.instance.contentResolver
        return Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun getDeviceDetails(): String {
        val tz = TimeZone.getDefault()
        val timeZone = " ${tz.id} ${tz.getDisplayName(false, TimeZone.SHORT)}"
        val androidVersion = Build.VERSION.RELEASE
        val buildVersion = BuildConfig.VERSION_CODE
        val androidName = Build.MODEL
        val locale = Locale.getDefault()
        return "{timeZone:$timeZone,android:$androidVersion,build:$buildVersion,device:$androidName,locale:$locale}"
    }
}