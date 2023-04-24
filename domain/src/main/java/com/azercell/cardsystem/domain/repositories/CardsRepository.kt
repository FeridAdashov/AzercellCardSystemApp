package com.azercell.cardsystem.domain.repositories

import com.azercell.cardsystem.domain.entity.BaseEntity
import com.azercell.cardsystem.domain.entity.CardsEntity

interface CardsRepository {

    /**
     * Get all cards
     */
    suspend fun getAllCards(): CardsEntity

    /**
     * Add card
     */
    suspend fun addCard(pan16: String, expireDate: String, cvv: String, cardName: String?): BaseEntity
}