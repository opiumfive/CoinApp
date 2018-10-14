package com.opiumfive.coinapp.ui.feature.main.view

import android.content.Context
import android.graphics.PorterDuff
import android.support.annotation.AttrRes
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_tabbar.view.*
import tech.snowfox.betholder.R

class TabBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, @AttrRes defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    enum class Tab {
        NONE,
        DASHBOARD,
        TRANSACTIONS,
        SETTINGS
    }

    var currentTab: Tab = Tab.NONE
    var itemClickListener: ((itemType: Tab) -> Unit)? = null
    private val selectedColorIcon by lazy { ContextCompat.getColor(context, R.color.tabSelectedColor) }
    private val unselectedColorIcon by lazy { ContextCompat.getColor(context, R.color.tabUselectedColor) }

    init {
        inflate(context, R.layout.view_tabbar, this)
        selectItem(Tab.NONE)
        dashboard.setOnClickListener { selectItem(Tab.DASHBOARD) }
        transactions.setOnClickListener { selectItem(Tab.TRANSACTIONS) }
        settings.setOnClickListener { selectItem(Tab.SETTINGS) }
    }

    fun selectItem(itemType: Tab?, trigger: Boolean = true) {
        itemType ?: return
        if (currentTab == itemType) return
        updateIndicator(itemType == Tab.DASHBOARD, dashboard)
        updateIndicator(itemType == Tab.TRANSACTIONS, transactions)
        updateIndicator(itemType == Tab.SETTINGS, settings)
        currentTab = itemType
        if (trigger) itemClickListener?.invoke(itemType)
    }

    private fun updateIndicator(selected: Boolean, imageView: ImageView) {
        imageView.setColorFilter( if (selected) selectedColorIcon else unselectedColorIcon, PorterDuff.Mode.SRC_ATOP)
    }
}