package com.opiumfive.coinapp.extension

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.setVisibility(visible: Boolean) {
    if (visible) visible() else gone()
}

fun ViewGroup.getChildViews(): List<View> {
    val childCount = childCount
    val list = mutableListOf<View>()
    for (i in 0 until childCount) {
        getChildAt(i)?.let { list.add(it) }
    }
    return list
}

fun EditText.onTextChanged(listener: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.let { listener.invoke(it.toString()) }
        }
    })
}

fun View.showKeyboard() {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE)
        as? InputMethodManager

    imm?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE)
        as? InputMethodManager

    this.windowToken?.let {
        imm?.hideSoftInputFromWindow(it, 0)
    }
}

fun EditText.onDoneClicked(hideKeyboard: Boolean = true, listener: () -> Unit) {
    this.setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            listener.invoke()
            if (hideKeyboard) this.hideKeyboard()
            return@setOnEditorActionListener true;
        }
        return@setOnEditorActionListener false;
    }
}