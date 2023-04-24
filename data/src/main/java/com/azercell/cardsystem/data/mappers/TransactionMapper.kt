package com.azercell.cardsystem.data.mappers

import com.azercell.cardsystem.data.dto.IncreaseCardBalanceResponse
import com.azercell.cardsystem.data.dto.MoneyTransferHistoryResponse
import com.azercell.cardsystem.data.dto.MoneyTransferResponseItem
import com.azercell.cardsystem.domain.entity.IncreaseCardBalanceEntity
import com.azercell.cardsystem.domain.entity.MoneyTransferHistoryEntity
import com.azercell.cardsystem.domain.entity.MoneyTransferHistoryEntityItem

class TransactionMapper : BaseMapper() {

    fun toMoneyTransferHistoryEntity(it: MoneyTransferHistoryResponse): MoneyTransferHistoryEntity {
        return MoneyTransferHistoryEntity(
            code = it.code,
            message = it.message,
            transactions = it.body?.map { toMoneyTransferEntityItem(it) }
        )
    }

    private fun toMoneyTransferEntityItem(it: MoneyTransferResponseItem?): MoneyTransferHistoryEntityItem {
        return MoneyTransferHistoryEntityItem(
            transactionId = it?.transactionId,
            fromCardId = it?.fromCardId,
            toCardId = it?.toCardId,
            fromCardPanMasked = it?.fromCardPanMasked,
            toCardPanMasked = it?.toCardPanMasked,
            operationDate = it?.operationDate,
            amount = it?.amount,
            currency = it?.currency,
            isIncome = it?.isIncome,
        )
    }

    fun toIncreaseCardBalanceEntity(it: IncreaseCardBalanceResponse): IncreaseCardBalanceEntity {
        return IncreaseCardBalanceEntity(
            code = it.code,
            message = it.message,
            balance = it.body?.balance
        )
    }
}