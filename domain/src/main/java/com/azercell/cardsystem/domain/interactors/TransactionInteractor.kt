package com.azercell.cardsystem.domain.interactors

import com.azercell.cardsystem.domain.entity.BaseEntity
import com.azercell.cardsystem.domain.entity.IncreaseCardBalanceEntity
import com.azercell.cardsystem.domain.entity.MoneyTransferHistoryEntity
import com.azercell.cardsystem.domain.entity.RequestResult
import com.azercell.cardsystem.domain.repositories.TransactionRepository

class TransactionInteractor(private val repository: TransactionRepository) : BaseInteractor() {

    suspend fun moneyTransfer(
        fromCardId: Int,
        toCardId: Int,
        amount: Double,
        currency: String
    ): RequestResult<BaseEntity> {
        return generateResult(repository.moneyTransfer(fromCardId, toCardId, amount, currency))
    }

    suspend fun getMoneyTransferHistory(cardId: Int): RequestResult<MoneyTransferHistoryEntity> {
        return generateResult(repository.getMoneyTransferHistory(cardId))
    }

    suspend fun increaseBalance(cardId: Int, amount: Double) : RequestResult<IncreaseCardBalanceEntity>{
        return generateResult(repository.increaseBalance(cardId, amount))
    }
}