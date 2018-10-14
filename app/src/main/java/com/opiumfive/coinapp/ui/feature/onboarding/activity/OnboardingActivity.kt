package com.opiumfive.coinapp.ui.feature.onboarding.activity

import android.os.Bundle
import android.os.Handler
import android.support.annotation.AnimRes
import android.view.View
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_onboarding.*
import org.jetbrains.anko.startActivity
import tech.snowfox.betholder.R
import com.opiumfive.coinapp.domain.di.component.AppComponent
import com.opiumfive.coinapp.extension.getChildViews
import com.opiumfive.coinapp.extension.gone
import com.opiumfive.coinapp.extension.onAnimationEnd
import com.opiumfive.coinapp.extension.visible
import com.opiumfive.coinapp.ui.base.BaseActivity
import com.opiumfive.coinapp.ui.feature.auth.activity.AuthActivity
import com.opiumfive.coinapp.ui.feature.main.activity.MainActivity
import com.opiumfive.coinapp.ui.feature.onboarding.model.OnboardingViewModel
import com.opiumfive.coinapp.ui.feature.newWallet.fragment.NewWalletDialogFragment

class OnboardingActivity : BaseActivity() {

    private val viewModel by lazy { initViewModel<OnboardingViewModel>() }

    override fun injectDI(appComponent: AppComponent) = appComponent.inject(this)

    private enum class State {
        WELLCOME,
        NOTIFICATIONS,
        SAFELY,
        FIRST_WALLET
    }

    private var state = State.WELLCOME

    private val blockButtonHandler by lazy { Handler() }
    private val enableButtonTask by lazy {
        Runnable { onboardingNext.isEnabled = true }
    }
    private val animationDuration by lazy {
        resources.getInteger(R.integer.onboarding_anim_duration).toLong()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (viewModel.isPostponedWalletLoaded() && viewModel.isTokenExist()) {
            startActivity<MainActivity>()
            finish()
            return
        }
        if (viewModel.isPostponedWalletLoaded() && viewModel.isTokenExist().not()) {
            startActivity<AuthActivity>()
            finish()
            return
        }
        setContentView(R.layout.activity_onboarding)
        setState(State.WELLCOME)

        onboardingNext.setOnClickListener {
            it.isEnabled = false
            nextState()
            blockButtonHandler.postDelayed(enableButtonTask, animationDuration)
        }

        onBoardingAddWallet.setOnClickListener {
            NewWalletDialogFragment.newInstance(NewWalletDialogFragment.NewWalletType.POSTPONED)
                .show(supportFragmentManager, NewWalletDialogFragment.TAG)
        }
    }

    private fun nextState() {
        when (state) {
            State.WELLCOME -> setState(State.NOTIFICATIONS)
            State.NOTIFICATIONS -> setState(State.SAFELY)
            State.SAFELY -> setState(State.FIRST_WALLET)
            else -> {
            }
        }
    }

    private fun setState(state: State) {
        when (state) {
            State.WELLCOME -> initWellcomeStep()
            State.NOTIFICATIONS -> initNotificationsStep()
            State.SAFELY -> initSafelyStep()
            State.FIRST_WALLET -> initFirstWalletStep()
        }
        this.state = state
    }

    private fun initWellcomeStep() {
        onboardingArrowUpCentral.visible()
        onboardingArrowUpRight.visible()
        onboardingArrowUpLeft.visible()
        onboardingArrowDownLeft.visible()
        onboardingArrowDownRight.visible()

        onboardingBlueChart.visible()
        onboardingBlackChart.visible()

        onboardingWellcomeTitle.visible()
        onboardingWellcomeMessage.visible()
    }

    private fun initNotificationsStep() {
        hideView(onboardingBlueChart, R.anim.jump_out_horizontal_right)
        hideView(onboardingBlackChart, R.anim.jump_out_horizontal_left)

        hideView(onboardingWellcomeTitle, R.anim.jump_out_horizontal_left)
        hideView(onboardingWellcomeMessage, R.anim.jump_out_horizontal_left)

        showView(onboardingNotificationBlue, R.anim.jump_in_horizontal_left)
        showView(onboardingNotificationBlack, R.anim.jump_in_vertical_bottom)
        showView(onboardingNotificationRed, R.anim.jump_in_horizontal_right)

        showView(onboardingNotificationTitle, R.anim.jump_in_horizontal_right)
        showView(onboardingNotificationMessage, R.anim.jump_in_horizontal_right)
    }

    private fun initSafelyStep() {
        hideView(onboardingArrowUpCentral, R.anim.jump_out_vertical_top)
        hideView(onboardingArrowUpRight, R.anim.jump_out_vertical_top)
        hideView(onboardingArrowUpLeft, R.anim.jump_out_vertical_top)
        hideView(onboardingArrowDownLeft, R.anim.jump_out_vertical_bottom)
        hideView(onboardingArrowDownRight, R.anim.jump_out_vertical_bottom)

        hideView(onboardingNotificationBlue, R.anim.jump_out_vertical_bottom)
        hideView(onboardingNotificationBlack, R.anim.jump_out_vertical_bottom)
        hideView(onboardingNotificationRed, R.anim.jump_out_horizontal_right)

        hideView(onboardingNotificationTitle, R.anim.jump_out_horizontal_left)
        hideView(onboardingNotificationMessage, R.anim.jump_out_horizontal_left)

        showView(onboardingTankLeft, R.anim.jump_in_horizontal_right_angle)
        showView(onboardingTankCentr, R.anim.jump_in_horizontal_right_angle)
        showView(onboardingTankRight, R.anim.jump_in_horizontal_right_angle, 200)

        showView(onboardingHeliLeft, R.anim.jump_in_vertical_top)
        showView(onboardingHeliCentr, R.anim.jump_in_vertical_top, 200)
        showView(onboardingHeliRight, R.anim.jump_in_vertical_top, 100)

        showView(onboardingSafelyTitle, R.anim.jump_in_horizontal_right)
        showView(onboardingSafelyMessage, R.anim.jump_in_horizontal_right)
    }

    private fun initFirstWalletStep() {
        hideView(onboardingNext, R.anim.jump_out_horizontal_left)

        hideView(onboardingTankLeft, R.anim.jump_out_horizontal_left_angle)
        hideView(onboardingTankCentr, R.anim.jump_out_horizontal_left_angle)
        hideView(onboardingTankRight, R.anim.jump_out_horizontal_left_angle, 200)

        hideView(onboardingHeliLeft, R.anim.jump_out_vertical_top)
        hideView(onboardingHeliCentr, R.anim.jump_out_vertical_top, 200)
        hideView(onboardingHeliRight, R.anim.jump_out_vertical_top, 100)

        hideView(onboardingSafelyTitle, R.anim.jump_out_horizontal_left)
        hideView(onboardingSafelyMessage, R.anim.jump_out_horizontal_left)

        showView(onboardingBlueChartAddWallet, R.anim.jump_in_horizontal_right)
        showView(onboardingBox, R.anim.jump_in_vertical_bottom)
        showView(onboardingBoxFront, R.anim.jump_in_vertical_bottom)

        showView(onboardingAddWalletTitle, R.anim.jump_in_horizontal_right)
        showView(onboardingAddWalletMessage, R.anim.jump_in_horizontal_right)

        showView(onBoardingAddWallet, R.anim.jump_in_horizontal_right)

        onboardingAddWalletEthIcon.visible()
        onboardingAddWalletBtcIcon.visible()

        showView(onboardingAddWalletSupports, R.anim.jump_in_horizontal_right)
        showView(onboardingAddWalletEthAnd, R.anim.jump_in_horizontal_right)
        showView(onboardingAddWalletBtc, R.anim.jump_in_horizontal_right)
    }

    private fun showView(view: View, @AnimRes animRes: Int, delay: Long = 0L) {
        val anim = AnimationUtils.loadAnimation(this, animRes)
        if (delay > 0) anim.startOffset = delay
        view.startAnimation(anim)
        view.visible()
    }

    private fun hideView(view: View, @AnimRes animRes: Int, delay: Long = 0L) {
        val anim = AnimationUtils.loadAnimation(this, animRes)
        if (delay > 0) anim.startOffset = delay
        anim.onAnimationEnd { view.gone() }
        view.startAnimation(anim)
    }

    override fun onDestroy() {
        super.onDestroy()
        onboardingContainer?.getChildViews()?.forEach { it.clearAnimation() }
        blockButtonHandler.removeCallbacks(enableButtonTask)
    }
}
