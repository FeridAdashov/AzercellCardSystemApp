package com.azercell.cardsystem.data.dto

import com.google.gson.annotations.SerializedName

data class IncreaseCardBalanceResponse(
    @field:SerializedName("body")
    val body: IncreaseCardBalanceResponseItem? = null,
) : BaseResponse()