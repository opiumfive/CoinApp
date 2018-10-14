package com.opiumfive.coinapp.ui.base.view

import android.content.Context
import android.graphics.PorterDuff
import android.support.annotation.AttrRes
import android.support.v7.widget.AppCompatButton
import android.util.AttributeSet
import tech.snowfox.betholder.R
import com.opiumfive.coinapp.extension.getColorCompat

class MainButton
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null, @AttrRes defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {

    override fun onFinishInflate() {
        super.onFinishInflate()
        changeColor(isEnabled)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        changeColor(enabled)
    }

    private fun changeColor(enabled: Boolean) {
        background?.setColorFilter(
            if (enabled)
                context.getColorCompat(R.color.colorAccent)
            else
                context.getColorCompat(R.color.whiteTwo)
            , PorterDuff.Mode.MULTIPLY
        )
    }
}