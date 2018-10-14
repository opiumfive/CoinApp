package com.opiumfive.coinapp.ui.base

import android.app.Activity
import android.content.Intent

abstract class ActivityResultBaseActivity : BaseActivity() {

    private var requestCode: Int = 0
    private var success: ((Intent?) -> Unit)? = null
    private var canceled: ((Intent?) -> Unit)? = null

    var activityResultListener: ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit)? = null

    fun startActivityWithResult(
        intent: Intent,
        requestCode: Int,
        success: ((Intent?) -> Unit)? = null,
        canceled: ((Intent?) -> Unit)? = null
    ) {
        this.requestCode = requestCode
        this.success = success
        this.canceled = canceled
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        activityResultListener?.invoke(requestCode, resultCode, data)
        if (this.requestCode == requestCode) {
            when (resultCode) {
                Activity.RESULT_OK -> success?.invoke(data)
                Activity.RESULT_CANCELED -> canceled?.invoke(data)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        success = null
        canceled = null
        activityResultListener = null
    }
}