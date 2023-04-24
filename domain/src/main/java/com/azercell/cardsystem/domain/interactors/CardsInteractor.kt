package com.azercell.cardsystem.domain.interactors

import com.azercell.cardsystem.domain.entity.BaseEntity
import com.azercell.cardsystem.domain.entity.CardsEntity
import com.azercell.cardsystem.domain.entity.RequestResult
import com.azercell.cardsystem.domain.repositories.CardsRepository

class CardsInteractor(private val repository: CardsRepository) : BaseInteractor() {

    suspend fun getAllCards(): RequestResult<CardsEntity> {
        return generateResult(repository.getAllCards())
    }

    suspend fun addCard(
        pan16: String,
        expireDate: String,
        cvv: String,
        cardName: String?
    ): RequestResult<BaseEntity> {
        return generateResult(repository.addCard(pan16, expireDate, cvv, cardName))
    }
}