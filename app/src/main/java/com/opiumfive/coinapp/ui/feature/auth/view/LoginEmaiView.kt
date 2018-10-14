package com.opiumfive.coinapp.ui.feature.auth.view

import android.content.Context
import android.support.annotation.AttrRes
import android.util.AttributeSet
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_auth_login_email.view.*
import tech.snowfox.betholder.R
import com.opiumfive.coinapp.extension.onTextChanged

class LoginEmaiView
@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, @AttrRes defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var loginClicker: ((email: String, password: String) -> Unit)? = null

    private val email: String
        get() = loginEmail.text.toString()

    private val password: String
        get() = loginPassword.text.toString()

    init {
        inflate(context, R.layout.view_auth_login_email, this)

        initViews()
    }

    private fun initViews() {
        val textWatcher: (String) -> Unit = {
            loginButton.isEnabled = email.isNotEmpty() && password.isNotEmpty()
        }
        loginEmail.onTextChanged(textWatcher)
        loginPassword.onTextChanged(textWatcher)
        loginButton.setOnClickListener { loginClicker?.invoke(email, password) }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        loginButton.isEnabled = enabled
    }
}