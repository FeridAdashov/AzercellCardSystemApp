package com.azercell.cardsystem.domain.entity


class IncreaseCardBalanceEntity(
    code: Int,
    message: String? = null,
    val balance: Double? = null
) : BaseEntity(code, message)