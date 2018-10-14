package com.opiumfive.coinapp.ui.feature.auth.model

import android.arch.lifecycle.ViewModel
import com.opiumfive.coinapp.domain.useCase.AuthUseCase
import com.opiumfive.coinapp.ui.base.ActivityResultBaseActivity
import javax.inject.Inject

class AuthViewModel @Inject constructor(private val authUseCase: AuthUseCase) : ViewModel() {

    fun login(email: String, password: String) = authUseCase.login(email, password)

    fun loginSocial(
        activity: ActivityResultBaseActivity,
        socialType: AuthUseCase.SocialType
    ) = authUseCase.loginSocial(activity, socialType)

    fun register(
        name: String,
        email: String,
        password: String
    ) = authUseCase.registration(name, email, password)
}