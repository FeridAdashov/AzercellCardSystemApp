package com.azercell.cardsystem.domain.entity

import java.io.Serializable

data class CardEntityItem(
    val id: Int? = null,
    val pan16: String? = null,
    val panMasked: String? = null,
    val cvv: String? = null,
    val expireDate: String? = null,
    val cardName: String? = null,
    val cardImage: String? = null,
    val balance: Double? = null,
    val currency: String? = null
) : Serializable