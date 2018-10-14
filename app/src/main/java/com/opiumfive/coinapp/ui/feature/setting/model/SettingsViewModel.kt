package com.opiumfive.coinapp.ui.feature.setting.model

import android.arch.lifecycle.ViewModel
import com.opiumfive.coinapp.domain.useCase.AuthUseCase
import javax.inject.Inject

class SettingsViewModel @Inject constructor(private val authUseCase: AuthUseCase) : ViewModel() {

    fun logout() = authUseCase.logout()
}