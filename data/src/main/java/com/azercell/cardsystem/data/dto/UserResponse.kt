package com.azercell.cardsystem.data.dto

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @field:SerializedName("body")
    val body: UserResponseItem? = null,
) : BaseResponse()