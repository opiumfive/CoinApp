package com.opiumfive.coinapp.ui.feature.main.activity

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import tech.snowfox.betholder.R
import com.opiumfive.coinapp.domain.di.component.AppComponent
import com.opiumfive.coinapp.extension.showMessage
import com.opiumfive.coinapp.ui.base.BaseActivity
import com.opiumfive.coinapp.ui.base.BaseFragment
import com.opiumfive.coinapp.ui.feature.main.fragment.BalanceFragment
import com.opiumfive.coinapp.ui.feature.main.model.TestFragment
import com.opiumfive.coinapp.ui.feature.main.view.BottomSheetBehavior
import com.opiumfive.coinapp.ui.feature.main.view.TabBar
import com.opiumfive.coinapp.ui.feature.setting.fragment.SettingFragment
import com.opiumfive.coinapp.ui.feature.transactions.fragment.TransactionsFragment

const val KEY_SHOW_ERROR_MESSAGE = "KEY_SHOW_ERROR_MESSAGE"

class MainActivity : BaseActivity() {

    override fun injectDI(appComponent: AppComponent) = appComponent.inject(this)

    private val bottomSheetBehavior by lazy { BottomSheetBehavior.from(bottomSheet) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tabBar.itemClickListener = {
            when (it) {
                TabBar.Tab.DASHBOARD -> {
                    selectItem("") { BalanceFragment.newInstance() }
                }

                TabBar.Tab.TRANSACTIONS -> {
                    selectItem("") { TransactionsFragment.newInstance() }
                }

                TabBar.Tab.SETTINGS -> {
                    selectItem("") { SettingFragment.newInstance() }
                }
                else -> {Unit}
            }
        }

        setUpBottomSheet()

        if (savedInstanceState == null) {
            tabBar.selectItem(TabBar.Tab.DASHBOARD)
        }

        showErrorMessage()
    }

    private fun showErrorMessage() {
        val message = intent.getStringExtra(KEY_SHOW_ERROR_MESSAGE) ?: return
        showMessage(message)
    }

    private fun setUpBottomSheet() {
        bottomSheetBehavior.isHideable = false
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.setAnchorOffset(0.2f)
        bottomSheetBehavior.setAnchorSheetCallback(object : BottomSheetBehavior.AnchorSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {}

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    private fun selectItem(tag: String, createFragment: () -> BaseFragment) {
        createAndReplaceFragment(createFragment.invoke(), tag)
    }

    private fun createAndReplaceFragment(fragment: BaseFragment, tag: String) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.mainContainer, fragment, tag)
                .commitAllowingStateLoss()
    }

    private fun createAndReplaceBottomFragment(fragment: BaseFragment, tag: String) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment, tag)
                .commitAllowingStateLoss()
    }
}
