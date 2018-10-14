package com.opiumfive.coinapp.ui.feature.newWallet.fragment

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.fragment_dialog_new_wallet.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.cancelButton
import org.jetbrains.anko.okButton
import org.jetbrains.anko.startActivity
import tech.snowfox.betholder.R
import com.opiumfive.coinapp.data.serverModel.wallet.NewWalletResponse
import com.opiumfive.coinapp.data.uiModel.data.Data
import com.opiumfive.coinapp.data.uiModel.wallet.ClipboardWallet
import com.opiumfive.coinapp.data.uiModel.wallet.WalletType
import com.opiumfive.coinapp.domain.di.component.AppComponent
import com.opiumfive.coinapp.extension.onDoneClicked
import com.opiumfive.coinapp.extension.onTextChanged
import com.opiumfive.coinapp.extension.showKeyboard
import com.opiumfive.coinapp.extension.showMessage
import com.opiumfive.coinapp.ui.base.BaseActivity
import com.opiumfive.coinapp.ui.base.BaseBottomSheetDialogFragment
import com.opiumfive.coinapp.ui.feature.auth.activity.AuthActivity
import com.opiumfive.coinapp.ui.feature.newWallet.activity.KEY_QR_WALLET_ADDRESS
import com.opiumfive.coinapp.ui.feature.newWallet.activity.QrWalletAddressActivity
import com.opiumfive.coinapp.ui.feature.newWallet.model.NewWalletViewModel

private const val REQUEST_CODE_QR = 10
private const val KEY_NEW_WALLET_TYPE = "KEY_NEW_WALLET_TYPE"

class NewWalletDialogFragment : BaseBottomSheetDialogFragment() {

    private val viewModel by lazy { initViewModel<NewWalletViewModel>() }

    enum class NewWalletType {
        POSTPONED,
        POST
    }

    private val clipboardObserver by lazy {
        Observer<ClipboardWallet> {
            it?.let { involveClipboardObserverResult(it) }
        }
    }

    private val observer by lazy {
        Observer<Data<NewWalletResponse>> {
            it ?: return@Observer
            initWalletResponse(it)
        }
    }

    private var clipboardDialog: AlertDialog? = null

    private var walletType: WalletType? = null

    private var newWalletType = NewWalletType.POSTPONED

    override fun injectDI(appComponent: AppComponent) = appComponent.inject(this)

    companion object {
        val TAG = NewWalletDialogFragment::class.java.simpleName ?: ""

        fun newInstance(newWalletType: NewWalletType) = NewWalletDialogFragment().apply {
            arguments = Bundle().apply {
                putSerializable(KEY_NEW_WALLET_TYPE, newWalletType)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        newWalletType = arguments?.getSerializable(KEY_NEW_WALLET_TYPE) as NewWalletType? ?:
            NewWalletType.POSTPONED
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener { _ ->
            val bottomSheet = dialog.findViewById<View>(android.support.design.R.id.design_bottom_sheet) as FrameLayout?
            bottomSheet?.let { BottomSheetBehavior.from(it).state = BottomSheetBehavior.STATE_EXPANDED }
            dialog.setOnShowListener(null)
        }
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_new_wallet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        newWalletClose.setOnClickListener { dismissAllowingStateLoss() }

        newWalletAddress.onTextChanged {
            newWalletButton.isEnabled = it.isNotEmpty()
        }

        newWalletQrCode.setOnClickListener {
            startActivityForResult(
                Intent(context, QrWalletAddressActivity::class.java),
                REQUEST_CODE_QR
            )
        }

        newWalletName.onDoneClicked { handleClick() }
        newWalletButton.setOnClickListener { handleClick() }
    }

    override fun onStart() {
        super.onStart()
        checkClipboard()
    }

    private fun checkClipboard() {
        viewModel.checkClipboard().observe(this, clipboardObserver)
    }

    private fun involveClipboardObserverResult(clipboardWallet: ClipboardWallet) {
        val walletType = clipboardWallet.walletType
        val address = clipboardWallet.address
        if (isNewAddress(address).not()) return

        val message =
            getString(R.string.have_wallet_type_in_clipboard).format(walletType.getTitle(), address)

        if (clipboardDialog?.isShowing == true) clipboardDialog?.dismiss()
        clipboardDialog = context.alert(message) {
            okButton {
                initClipboardWallet(clipboardWallet)
            }
            cancelButton {}
        }.build()
        clipboardDialog?.show()
    }

    private fun initClipboardWallet(clipboardWallet: ClipboardWallet) {
        newWalletAddress.setText(clipboardWallet.address)
        walletType = clipboardWallet.walletType

        focusNameEdit()
    }

    private fun focusNameEdit() {
        newWalletName.requestFocus()
        newWalletName.postDelayed({ newWalletName.showKeyboard() }, 50)
    }

    private fun isNewAddress(address: String) = newWalletAddress.text.toString() != address

    private fun handleClick() {
        val address = newWalletAddress.text.toString()
        val name = newWalletName.text.toString()
        val clipboardWallet = viewModel.getClipboardWallet(address)
        if (clipboardWallet == null) {
            selectWalletType(address, name)
            return
        }
        val walletType = clipboardWallet.walletType
        addWallet(address, name, walletType)
    }

    private fun selectWalletType(address: String, name: String) {
        context.alert {
            title = getString(R.string.confirmation)
            message = getString(R.string.error_wallet_type)
            positiveButton(getString(R.string.btc_short).toUpperCase()) {
                addWallet(address, name, WalletType.BTC)
            }
            negativeButton(getString(R.string.eth_short).toUpperCase()) {
                addWallet(address, name, WalletType.ETH)
            }
            neutralPressed(android.R.string.cancel) {}
        }.show()
    }

    private fun addWallet(address: String, name: String, walletType: WalletType) {
        when (newWalletType){
            NewWalletType.POSTPONED -> {
                viewModel.addPostponedWallet(address, name, walletType)
                (context as BaseActivity).apply {
                    startActivity<AuthActivity>()
                    finish()
                }
            }

            else -> viewModel.addWallet(address, name, walletType).observe(this, observer)
        }
    }

    private fun initWalletResponse(data: Data<NewWalletResponse>) {
        newWalletButton.isEnabled = data.isLoading().not()

        if (data.isSuccess()) {
            (context as BaseActivity).apply {
                startActivity<AuthActivity>()
                finish()
            }
        }

        if (data.isError()) {
            context.showMessage(data.throwable?.message)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_QR) {
            val address = data?.getStringExtra(KEY_QR_WALLET_ADDRESS)
            val clipboardWallet = viewModel.getClipboardWallet(address) ?: return
            initClipboardWallet(clipboardWallet)
        }
    }
}