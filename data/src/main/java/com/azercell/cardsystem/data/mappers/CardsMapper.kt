package com.azercell.cardsystem.data.mappers

import com.azercell.cardsystem.data.dto.CardsResponse
import com.azercell.cardsystem.data.dto.CardsResponseItem
import com.azercell.cardsystem.domain.entity.CardEntityItem
import com.azercell.cardsystem.domain.entity.CardsEntity

class CardsMapper : BaseMapper() {

    fun toCardsEntity(it: CardsResponse): CardsEntity {
        return CardsEntity(
            code = it.code,
            message = it.message,
            cards = it.body?.map { toCardEntityItem(it) }
        )
    }

    private fun toCardEntityItem(it: CardsResponseItem?): CardEntityItem {
        return CardEntityItem(
            id = it?.id ?: 0,
            cardName = it?.cardName ?: "",
            pan16 = it?.pan16 ?: "",
            panMasked = it?.panMasked ?: "",
            cvv = it?.cvv ?: "",
            expireDate = it?.expireDate ?: "",
            cardImage = it?.cardImage ?: "",
            balance = it?.balance ?: 0.0,
            currency = it?.currency ?: "AZN",
        )
    }
}