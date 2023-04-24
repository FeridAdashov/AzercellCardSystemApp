package com.azercell.cardsystem.common.extensions

import java.util.*

fun String.capitalize(): String =
    this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

fun String.maskPan16WithStars(): String =
    this.replace("(?<!^..).(?=.{3})".toRegex(), "*")