package com.opiumfive.coinapp.domain

import android.content.Context
import android.content.SharedPreferences
import com.opiumfive.coinapp.data.bdModel.wallet.PostponedWallet
import javax.inject.Inject
import javax.inject.Singleton

private const val KEY_TOKEN = "KEY_1"
private const val KEY_LOGIN = "KEY_2"
private const val KEY_LAST_TRANSACTION_TIMESTAMP = "KEY_3"
private const val KEY_ONESIGNAL_PLAYER_ID = "KEY_4"
private const val KEY_POSTPONED_WALLED = "KEY_5"
private const val KEY_POSTPONED_WALLED_LOADED = "KEY_6"

@Singleton
class Prefs @Inject constructor(context: Context) {
    private val sharedPreferences: SharedPreferences

    init {
        val sharedPrefName = context.packageName + "_preferences"
        sharedPreferences = context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE)
    }

    fun saveToken(accessToken: String?) {
        saveString(KEY_TOKEN, accessToken)
    }

    fun getToken(): String? = sharedPreferences.getString(KEY_TOKEN, null)

    fun saveLogin(login: String?){
        saveString(KEY_LOGIN, login)
    }

    fun getLogin(): String? = sharedPreferences.getString(KEY_LOGIN, null)

    fun saveLastTransactionTimestamp(walletId: Int, timestamp: Long){
        saveLong("$KEY_LAST_TRANSACTION_TIMESTAMP$walletId", timestamp)
    }

    fun getLastTransactionTimestamp(walletId: Int): Long {
        return sharedPreferences.getLong("$KEY_LAST_TRANSACTION_TIMESTAMP$walletId", 0)
    }

    fun saveOnesignalPlayerId(playerId: String?){
        saveString(KEY_ONESIGNAL_PLAYER_ID, playerId)
    }

    fun getOnesignalPlayerId(): String? = sharedPreferences.getString(KEY_ONESIGNAL_PLAYER_ID, null)

    fun savePostponedWallet(postponedWallet: PostponedWallet) {
        saveString(KEY_POSTPONED_WALLED, postponedWallet.wrap())
    }

    fun getPostponedWallet(): PostponedWallet? {
        val wrapString = sharedPreferences.getString(KEY_POSTPONED_WALLED, null)
        return PostponedWallet.unwrap(wrapString)
    }

    fun setPostponedWalletStatus(loaded: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_POSTPONED_WALLED_LOADED, loaded).apply()
    }

    fun isPostponedWalletLoaded(): Boolean {
        return sharedPreferences.getBoolean(KEY_POSTPONED_WALLED_LOADED, false)
    }

    private fun saveLong(key: String, value: Long?){
        sharedPreferences.edit().putLong(key, value ?: 0).apply()
    }

    private fun saveString(key: String, value: String?) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun clear() {
        // Save last login
        val lastLogin = getLogin()
        sharedPreferences.edit().clear().apply()
        saveString(KEY_LOGIN, lastLogin)
    }
}