package com.opiumfive.coinapp.domain.useCase

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.content.ClipboardManager
import android.content.Context
import com.opiumfive.coinapp.data.bdModel.wallet.PostponedWallet
import com.opiumfive.coinapp.data.uiModel.wallet.ClipboardWallet
import com.opiumfive.coinapp.data.uiModel.wallet.WalletType
import com.opiumfive.coinapp.domain.repository.wallet.IWalletRepository
import java.util.regex.Pattern
import javax.inject.Inject

private const val PATTERN_BTC = "\\b([13][^\\WO0Il+/]{25,34})|(bc1[^\\W1bio]{6,89})(?:\\b|\$)"
private const val PATTERN_ETH = "(0x[a-f0-9]{40})(?:\$|[^a-f0-9]+?)"

class NewWalletUseCase @Inject constructor(
    context: Context,
    private val repository: IWalletRepository
) {
    private val btcPattern = Pattern.compile(PATTERN_BTC)
    private val ethPattern = Pattern.compile(PATTERN_ETH, Pattern.CASE_INSENSITIVE)

    private val clipboardManager =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    fun checkClipboard(checkCurrentWallets: Boolean): LiveData<ClipboardWallet> {
        val address = clipboardManager.primaryClip?.getItemAt(0)?.text
            ?: return MediatorLiveData<ClipboardWallet>()
        return checkAddress(address.toString(), checkCurrentWallets)
    }

    private fun checkAddress(address: String?, checkCurrentWallets: Boolean): LiveData<ClipboardWallet> {
        val liveData = MediatorLiveData<ClipboardWallet>()
        address ?: return liveData
        if (checkCurrentWallets.not()) {
            val clipboardWallet = getClipboardWallet(address)
            liveData.postValue(clipboardWallet)
            return liveData
        }

        // TODO if need to check current wallets
//        val source = repository.getWallets()
//        liveData.addSource(source) {
//            if (it?.isLoading() == true) return@addSource
//            liveData.removeSource(source)
//
//            val containsAddress = it?.body?.any { it.address == address }
//            if (containsAddress == true) return@addSource
//            val clipboardWallet = getClipboardWallet(address) ?: return@addSource
//            liveData.postValue(clipboardWallet)
//        }

        return liveData
    }

    fun getClipboardWallet(address: String?): ClipboardWallet? {
        address ?: return null
        val ethMatcher = ethPattern.matcher(address)
        val btcMatcher = btcPattern.matcher(address)
        return when {
            ethMatcher.find() -> {
                ClipboardWallet(ethMatcher.group(), WalletType.ETH)
            }
            btcMatcher.find() -> {
                ClipboardWallet(btcMatcher.group(), WalletType.BTC)
            }
            else -> null
        }
    }

    fun isAddressValid(currency: String?, walletType: WalletType?): Boolean {
        return when (walletType) {
            WalletType.BTC -> btcPattern.matcher(currency).matches()
            WalletType.ETH -> ethPattern.matcher(currency).matches()
            else -> false
        }
    }

    fun addWallet(
        address: String,
        walletName: String,
        walletType: WalletType
    ) = repository.addWallet(address, walletName, walletType)

    fun addPostponedWallet(
        address: String,
        walletName: String,
        walletType: WalletType
    ) = repository.addPostponedWallet(PostponedWallet(address, walletName, walletType))
}