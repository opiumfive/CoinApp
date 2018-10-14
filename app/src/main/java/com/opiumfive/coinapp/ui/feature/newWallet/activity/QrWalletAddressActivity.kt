package com.opiumfive.coinapp.ui.feature.newWallet.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import com.fondesa.kpermissions.extension.listeners
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import kotlinx.android.synthetic.main.activity_qr_wallet_address.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.cancelButton
import org.jetbrains.anko.okButton
import tech.snowfox.betholder.BuildConfig
import tech.snowfox.betholder.R
import com.opiumfive.coinapp.domain.di.component.AppComponent
import com.opiumfive.coinapp.ui.base.BaseActivity
import com.opiumfive.coinapp.ui.feature.newWallet.model.QrWalletViewModel
import android.content.pm.PackageManager
import android.view.Window
import android.view.WindowManager
import com.opiumfive.coinapp.extension.gone


const val KEY_QR_WALLET_ADDRESS = "KEY_QR_WALLET_ADDRESS"

class QrWalletAddressActivity : BaseActivity(), BarcodeCallback {

    private val viewModel by lazy { initViewModel<QrWalletViewModel>() }

    override fun injectDI(appComponent: AppComponent) = appComponent.inject(this)

    private val permissionRequest by lazy {
        permissionsBuilder(Manifest.permission.CAMERA).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_qr_wallet_address)

        initViews()
        initPermissionRequest()
    }

    private fun initViews() {
        qrWalletBack.setOnClickListener { onBackPressed() }

        when (hasFlash()) {
            true -> qrWalletFlash.setOnClickListener { switchFlash() }
            false -> qrWalletFlash.gone()
        }

        qrWalletBarcodeView.decodeContinuous(this)
    }

    private fun switchFlash() {
        val isTorchOn = qrWalletFlashTitle.text == getString(R.string.flash_on)
        when (isTorchOn) {
            true -> {
                qrWalletFlashIcon.setImageResource(R.drawable.ic_flash_off)
                qrWalletFlashTitle.setText(R.string.flash_off)
                qrWalletBarcodeView.setTorchOff()
            }
            false -> {
                qrWalletFlashIcon.setImageResource(R.drawable.ic_flash_on)
                qrWalletFlashTitle.setText(R.string.flash_on)
                qrWalletBarcodeView.setTorchOn()
            }
        }
    }

    private fun initPermissionRequest() {
        permissionRequest.listeners {
            onAccepted {
                qrWalletBarcodeView.resume()
            }

            onDenied { cameraDenied() }

            onPermanentlyDenied { cameraDenied() }

            onShouldShowRationale { _, permissionNonce ->
                alert(R.string.ask_camera_permission) {
                    okButton { permissionNonce.use() }
                    cancelButton { cameraDenied() }
                }.show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        permissionRequest.send()
    }

    override fun onStop() {
        super.onStop()
        qrWalletBarcodeView.pause()
    }

    override fun barcodeResult(result: BarcodeResult?) {
        val resultText = result?.text ?: return
        val clipboardWallet = viewModel.getClipboardWallet(resultText) ?: return
        handleResult(clipboardWallet.address)
    }

    override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {}

    private fun cameraDenied() {
        alert(R.string.camera_permission_denied) {
            okButton {
                startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                    )
                )
            }

            cancelButton { finish() }
        }
    }

    private fun handleResult(address: String) {
        setResult(Activity.RESULT_OK, Intent().apply { putExtra(KEY_QR_WALLET_ADDRESS, address) })
        finish()
    }

    private fun hasFlash(): Boolean {
        return applicationContext.packageManager
            .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
    }
}
