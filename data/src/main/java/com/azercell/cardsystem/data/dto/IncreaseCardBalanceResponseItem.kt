package com.azercell.cardsystem.data.dto

import com.google.gson.annotations.SerializedName

data class IncreaseCardBalanceResponseItem(
    @field:SerializedName("balance")
    val balance: Double? = null,
)
