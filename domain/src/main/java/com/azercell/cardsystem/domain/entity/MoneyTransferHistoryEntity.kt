package com.azercell.cardsystem.domain.entity


class MoneyTransferHistoryEntity(
    code: Int,
    message: String? = null,
    val transactions: List<MoneyTransferHistoryEntityItem>? = null
) : BaseEntity(code, message)