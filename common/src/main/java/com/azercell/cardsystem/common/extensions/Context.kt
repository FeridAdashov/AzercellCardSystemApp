package com.azercell.cardsystem.common.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager

fun Context.hideKeyboard(currentFocus: View) {
    currentFocus.clearFocus()
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    imm.hideSoftInputFromWindow(
        currentFocus.windowToken,
        InputMethodManager.HIDE_NOT_ALWAYS
    )
}


fun Context.verticalLayoutManager(): LinearLayoutManager {
    return LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
}

fun Context.horizontalLayoutManager(): LinearLayoutManager {
    return LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
}