package com.azercell.cardsystem.domain.entity

import java.io.Serializable

data class UserEntityItem(
    val name: String,
    val surname: String,
    val birthday: String,
    val mobile: String,
) : Serializable