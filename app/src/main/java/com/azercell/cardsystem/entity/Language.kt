package com.azercell.cardsystem.entity

import com.azercell.cardsystem.R
import com.azercell.cardsystem.base.BaseApplication

enum class Language(val value: String) {
    AZ("az"),
    EN("en"),
    RU("ru")
}

fun Language.tr(): String {
    return when (this) {
        Language.AZ -> BaseApplication.applicationContext().getString(R.string.az)
        Language.EN -> BaseApplication.applicationContext().getString(R.string.en)
        Language.RU -> BaseApplication.applicationContext().getString(R.string.ru)
    }
}

fun mapLanguage(lang: String?): Language {
    return when (lang) {
        "en" -> Language.EN
        "ru" -> Language.RU
        else -> Language.AZ
    }
}