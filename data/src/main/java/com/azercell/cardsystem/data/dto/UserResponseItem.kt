package com.azercell.cardsystem.data.dto

import com.google.gson.annotations.SerializedName

data class UserResponseItem(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("surname")
    val surname: String? = null,

    @field:SerializedName("birthday")
    val birthday: String? = null,

    @field:SerializedName("mobile")
    val mobile: String? = null,
)
