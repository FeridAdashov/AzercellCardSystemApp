package com.azercell.cardsystem.common.widgets.buttons

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.button.MaterialButton
import com.azercell.cardsystem.common.extensions.dpToPx


class CustomRoundedButton : MaterialButton {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setupAttrs(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        setupAttrs(context, attrs)
    }

    private fun setupAttrs(context: Context, attrs: AttributeSet?) {
        height = 56.dpToPx(context).toInt()
        cornerRadius = 28.dpToPx(context).toInt()
        iconGravity = ICON_GRAVITY_TEXT_START
        iconPadding = 16.dpToPx(context).toInt()
        insetTop = 1
        insetBottom = 1
        isAllCaps = false
    }
}