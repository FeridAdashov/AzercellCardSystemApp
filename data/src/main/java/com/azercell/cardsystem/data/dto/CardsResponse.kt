package com.azercell.cardsystem.data.dto

import com.google.gson.annotations.SerializedName

data class CardsResponse(
    @field:SerializedName("body")
    val body: List<CardsResponseItem>? = null,
) : BaseResponse()