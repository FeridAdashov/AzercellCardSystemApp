package com.azercell.cardsystem.common.widgets.textviews

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.*
import android.graphics.drawable.shapes.RoundRectShape
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.azercell.cardsystem.common.R
import java.util.*
import kotlin.math.roundToInt


class BetterTextView : AppCompatTextView {

    private var gradientDrawable: Drawable? = null

    private var cornerRadius: Float = 0f

    private var cornerRadii = arrayListOf(
        0.0f,
        0.0f,
        0.0f,
        0.0f,
        0.0f,
        0.0f,
        0.0f,
        0.0f,
    )
    private var borderWidth: Float = 0f
    private var borderColor: Int = Color.TRANSPARENT

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
        val a = context.obtainStyledAttributes(attrs, R.styleable.BetterView)

        cornerRadius = a.getDimension(R.styleable.BetterView_cornerRadius, 0f)
        borderWidth = a.getDimension(R.styleable.BetterView_borderWidth, 0f)
        borderColor = a.getColor(R.styleable.BetterView_borderColor, Color.TRANSPARENT)
        val rippleColor = a.getColor(R.styleable.BetterView_rippleColor, Color.TRANSPARENT)

        val topLeftRadius = a.getDimension(R.styleable.BetterView_topLeftRadius, 0f)
        val topRightRadius = a.getDimension(R.styleable.BetterView_topRightRadius, 0f)
        val bottomRightRadius = a.getDimension(R.styleable.BetterView_bottomRightRadius, 0f)
        val bottomLeftRadius = a.getDimension(R.styleable.BetterView_bottomLeftRadius, 0f)

        cornerRadii = arrayListOf(
            topLeftRadius,
            topLeftRadius,
            topRightRadius,
            topRightRadius,
            bottomRightRadius,
            bottomRightRadius,
            bottomLeftRadius,
            bottomLeftRadius
        )


        if (rippleColor != Color.TRANSPARENT) {
            if (background != null) {
                val colorDrawable = background as ColorDrawable
                gradientDrawable = getAdaptiveRippleDrawable(
                    normalColor = colorDrawable.color,
                    pressedColor = rippleColor,
                    radius = cornerRadius,
                    borderColor = borderColor,
                    borderWidth = borderWidth
                )
                background = gradientDrawable
            }
        } else {
            setDrawableWithCornerRadius()
        }
        isFocusable = true
        isClickable = true
        includeFontPadding = false

        a.recycle()


    }

    private fun setDrawableWithCornerRadius() {
        val colorDrawable = background?.let {
            it as ColorDrawable
        } ?: run {
            ColorDrawable(Color.TRANSPARENT)
        }

        val contentDrawable = GradientDrawable()
        contentDrawable.setColor(colorDrawable.color)
        if (cornerRadius != 0f) {
            contentDrawable.cornerRadius = cornerRadius
        } else {
            contentDrawable.cornerRadii = cornerRadii.toFloatArray()
        }
        contentDrawable.setStroke(borderWidth.roundToInt(), borderColor)
        gradientDrawable = contentDrawable
        background = gradientDrawable
    }

    private fun getAdaptiveRippleDrawable(
        normalColor: Int, pressedColor: Int,
        radius: Float,
        borderWidth: Float,
        borderColor: Int,
    ): RippleDrawable {

        val colorDrawable = background as ColorDrawable

        val contentDrawable = GradientDrawable()
        contentDrawable.setColor(colorDrawable.color)
        contentDrawable.cornerRadius = radius
        contentDrawable.setStroke(borderWidth.roundToInt(), borderColor)
        return RippleDrawable(
            ColorStateList.valueOf(pressedColor),
            contentDrawable, getRippleMask(normalColor, radius)
        )
    }

    private fun getRippleMask(color: Int, radius: Float): Drawable {
        val outerRadii = FloatArray(8)
        // 3 is radius of final ripple,
        // instead of 3 you can give required final radius
        Arrays.fill(outerRadii, radius)
        val r = RoundRectShape(outerRadii, null, outerRadii)
        val shapeDrawable = ShapeDrawable(r)
        shapeDrawable.paint.color = color
        return shapeDrawable
    }

    override fun setBackgroundColor(color: Int) {
        super.setBackgroundColor(color)
        setDrawableWithCornerRadius()
        background = gradientDrawable
    }


}