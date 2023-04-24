package com.azercell.cardsystem.data.dto

import com.google.gson.annotations.SerializedName

data class MoneyTransferHistoryResponse(
    @field:SerializedName("body")
    val body: List<MoneyTransferResponseItem>? = null,
) : BaseResponse()