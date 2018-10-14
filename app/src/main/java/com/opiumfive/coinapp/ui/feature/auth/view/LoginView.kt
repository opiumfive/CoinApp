package com.opiumfive.coinapp.ui.feature.auth.view

import android.content.Context
import android.support.annotation.AttrRes
import android.util.AttributeSet
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_auth_login.view.*
import org.jetbrains.anko.toast
import tech.snowfox.betholder.R
import com.opiumfive.coinapp.domain.useCase.AuthUseCase

class LoginView
@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, @AttrRes defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var loginListener: ((socialType: AuthUseCase.SocialType) -> Unit)? = null
    var emailListener: (() -> Unit)? = null

    init {
        inflate(context, R.layout.view_auth_login, this)

        initViews()
    }

    private fun initViews() {
        loginIcos.setOnClickListener {
            loginListener?.invoke(AuthUseCase.SocialType.ICOS_ID)
        }

        loginFacebook.setOnClickListener {
            loginListener?.invoke(AuthUseCase.SocialType.FACEBOOK)
        }

        loginGoogle.setOnClickListener {
            loginListener?.invoke(AuthUseCase.SocialType.GOOGLE)
        }

        loginEmail.setOnClickListener {
            emailListener?.invoke()
        }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        loginIcos.isEnabled = enabled
        loginFacebook.isEnabled = enabled
        loginGoogle.isEnabled = enabled
        loginEmail.isEnabled = enabled
    }
}