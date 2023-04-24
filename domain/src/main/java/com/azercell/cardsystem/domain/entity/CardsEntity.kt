package com.azercell.cardsystem.domain.entity


class CardsEntity(
    code: Int,
    message: String? = null,
    val cards: List<CardEntityItem>? = null
) : BaseEntity(code, message)