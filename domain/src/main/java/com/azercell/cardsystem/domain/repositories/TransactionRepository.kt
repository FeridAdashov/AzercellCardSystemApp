package com.azercell.cardsystem.domain.repositories

import com.azercell.cardsystem.domain.entity.BaseEntity
import com.azercell.cardsystem.domain.entity.IncreaseCardBalanceEntity
import com.azercell.cardsystem.domain.entity.MoneyTransferHistoryEntity

interface TransactionRepository {

    /**
     * Get all transfer history
     */
    suspend fun getMoneyTransferHistory(cardId: Int): MoneyTransferHistoryEntity

    /**
     * Card to card transfer
     */
    suspend fun moneyTransfer(
        fromCardId: Int,
        toCardId: Int,
        amount: Double,
        currency: String
    ): BaseEntity


    /**
     * Increase card balance
     */
    suspend fun increaseBalance(cardId: Int, amount: Double): IncreaseCardBalanceEntity
}