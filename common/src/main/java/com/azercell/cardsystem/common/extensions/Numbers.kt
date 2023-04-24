package com.azercell.cardsystem.common.extensions

import android.content.Context
import kotlin.math.round

fun Int.dpToPx(context: Context): Float = this * context.resources.displayMetrics.density

fun Float.pxToDp(context: Context): Int = (this / context.resources.displayMetrics.density).toInt()

fun Float.round(decimals: Int): Float {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return (round(this * multiplier) / multiplier).toFloat()
}

fun Double?.decimalPlace(place: Int): String = String.format("%.${place}d", this ?: 0.0f)
