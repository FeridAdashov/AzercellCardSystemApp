package com.azercell.cardsystem.entity

import com.azercell.cardsystem.R

enum class SettingType(val menuIndex: Int) {
    PERSONAL_INFO(0),
    LANGUAGE(1),
    LOG_OUT(2)
}

data class SettingAdapterItemData(
    val title: String,
    var subTitle: String? = null,
    val icon: Int = R.drawable.ic_settings,
    val userAvatarUrl: String? = null,
    val type: SettingType
)