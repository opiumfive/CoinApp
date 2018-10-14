package com.opiumfive.coinapp.ui.feature.onboarding.model

import android.arch.lifecycle.ViewModel
import com.opiumfive.coinapp.domain.Prefs
import com.opiumfive.coinapp.domain.useCase.AuthUseCase
import javax.inject.Inject

class OnboardingViewModel @Inject constructor(private val prefs: Prefs) : ViewModel() {

    fun isPostponedWalletLoaded() = prefs.isPostponedWalletLoaded()

    fun isTokenExist() = prefs.getToken() != null
}