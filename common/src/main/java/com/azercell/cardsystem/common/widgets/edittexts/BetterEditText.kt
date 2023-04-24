package com.azercell.cardsystem.common.widgets.edittexts

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import com.azercell.cardsystem.common.R
import com.azercell.cardsystem.common.extensions.hideKeyboard


class BetterEditText : AppCompatEditText,
    TextView.OnEditorActionListener {

    private val gradientDrawable = GradientDrawable()
    private var cornerRadius: Float = 0f

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setupAttrs(context, attrs)
        setOnEditorActionListener(this)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        setupAttrs(context, attrs)
        setOnEditorActionListener(this)
    }


    @SuppressLint("CustomViewStyleable")
    private fun setupAttrs(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.BetterView)

        val cornerRadius = a.getDimension(R.styleable.BetterView_cornerRadius, 0f)

        if (cornerRadius > 0) {
            gradientDrawable.cornerRadius = cornerRadius
        } else {
            val topLeftRadius = a.getDimension(R.styleable.BetterView_topLeftRadius, 0f)
            val topRightRadius = a.getDimension(R.styleable.BetterView_topRightRadius, 0f)
            val bottomRightRadius = a.getDimension(R.styleable.BetterView_bottomRightRadius, 0f)
            val bottomLeftRadius = a.getDimension(R.styleable.BetterView_bottomLeftRadius, 0f)

            gradientDrawable.cornerRadii = floatArrayOf(
                topLeftRadius,
                topLeftRadius,
                topRightRadius,
                topRightRadius,
                bottomRightRadius,
                bottomRightRadius,
                bottomLeftRadius,
                bottomLeftRadius
            )
        }
        if (background != null) {
            val colorDrawable = background as ColorDrawable
            gradientDrawable.setColor(colorDrawable.color)
            background = gradientDrawable
        }

        a.recycle()
    }

    override fun setBackgroundColor(color: Int) {
        super.setBackgroundColor(color)
        val colorDrawable = background as ColorDrawable
        gradientDrawable.cornerRadius = cornerRadius
        gradientDrawable.setColor(colorDrawable.color)
        background = gradientDrawable
    }


    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            clearFocus()
        }
        return super.onKeyPreIme(keyCode, event)
    }


    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            context.hideKeyboard(this)
            clearFocus()
        }
        return false
    }
}