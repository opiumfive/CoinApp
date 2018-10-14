package com.opiumfive.coinapp.extension

import android.view.animation.Animation

inline fun Animation.onAnimationEnd(crossinline onAnimationEnd: () -> Unit) {
    setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(p0: Animation?) {}
        override fun onAnimationEnd(p0: Animation?) {
            onAnimationEnd()
        }

        override fun onAnimationStart(p0: Animation?) {}
    })
}