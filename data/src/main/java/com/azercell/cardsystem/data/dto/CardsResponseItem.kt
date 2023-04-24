package com.azercell.cardsystem.data.dto

import com.google.gson.annotations.SerializedName

data class CardsResponseItem(
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("pan16")
    val pan16: String? = null,

    @field:SerializedName("panMasked")
    val panMasked: String? = null,

    @field:SerializedName("expireDate")
    val expireDate: String? = null,

    @field:SerializedName("cvv")
    val cvv: String? = null,

    @field:SerializedName("cardName")
    val cardName: String? = null,

    @field:SerializedName("cardImage")
    val cardImage: String? = null,

    @field:SerializedName("balance")
    var balance: Double? = null,

    @field:SerializedName("currency")
    val currency: String? = null
)
