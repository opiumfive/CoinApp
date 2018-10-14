package com.opiumfive.coinapp.ui.feature.auth.view

import android.content.Context
import android.support.annotation.AttrRes
import android.util.AttributeSet
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_auth_register.view.*
import tech.snowfox.betholder.R
import com.opiumfive.coinapp.extension.onTextChanged

class RegisterView
@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, @AttrRes defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var registerListener: ((name: String, email: String, password: String) -> Unit)? = null

    private val name: String
        get() = registerName.text.toString()

    private val email: String
        get() = registerEmail.text.toString()

    private val password: String
        get() = registerPassword.text.toString()

    init {
        inflate(context, R.layout.view_auth_register, this)

        initViews()
    }

    private fun initViews() {
        val textWatcher: (String) -> Unit = {
            registerButton.isEnabled = name.isNotEmpty() &&
                email.isNotEmpty() &&
                password.isNotEmpty()
        }
        registerName.onTextChanged(textWatcher)
        registerEmail.onTextChanged(textWatcher)
        registerPassword.onTextChanged(textWatcher)

        registerButton.setOnClickListener { registerListener?.invoke(name, email, password) }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        registerButton.isEnabled = enabled
    }
}