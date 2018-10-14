package com.opiumfive.coinapp.ui.feature.setting.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_settings.*
import org.jetbrains.anko.startActivity
import tech.snowfox.betholder.R
import com.opiumfive.coinapp.data.serverModel.BaseResponse
import com.opiumfive.coinapp.data.uiModel.data.Data
import com.opiumfive.coinapp.domain.di.component.AppComponent
import com.opiumfive.coinapp.ui.base.BaseFragment
import com.opiumfive.coinapp.ui.feature.onboarding.activity.OnboardingActivity
import com.opiumfive.coinapp.ui.feature.setting.model.SettingsViewModel

class SettingFragment : BaseFragment() {

    override fun injectDI(appComponent: AppComponent) = appComponent.inject(this)

    private val viewModel by lazy { initViewModel<SettingsViewModel>() }

    private val observer by lazy {
        Observer<Data<BaseResponse>> {
            it?.let { handleResponse(it) }
        }
    }

    companion object {
        fun newInstance() = SettingFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingsLogout.setOnClickListener {
            viewModel.logout().observe(this, observer)
        }
    }

    private fun handleResponse(data: Data<BaseResponse>) {
        if (data.isSuccess()){
            context.startActivity<OnboardingActivity>()
            activityNotNull.finishAffinity()
        }
    }
}