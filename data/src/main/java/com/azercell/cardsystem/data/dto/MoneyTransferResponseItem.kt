package com.azercell.cardsystem.data.dto

import com.google.gson.annotations.SerializedName

data class MoneyTransferResponseItem(
    @field:SerializedName("transactionId")
    val transactionId: Int? = null,

    @field:SerializedName("fromCardId")
    val fromCardId: Int? = null,

    @field:SerializedName("toCardId")
    val toCardId: Int? = null,

    @field:SerializedName("fromCardPanMasked")
    val fromCardPanMasked: String? = null,

    @field:SerializedName("toCardPanMasked")
    val toCardPanMasked: String? = null,

    @field:SerializedName("operationDate")
    val operationDate: String? = null,

    @field:SerializedName("amount")
    val amount: Double? = null,

    @field:SerializedName("currency")
    val currency: String? = null,

    @field:SerializedName("isIncome")
    var isIncome: Boolean? = null,
)
