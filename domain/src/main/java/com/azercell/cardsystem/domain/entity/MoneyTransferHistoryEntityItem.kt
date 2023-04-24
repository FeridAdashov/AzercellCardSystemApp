package com.azercell.cardsystem.domain.entity

import java.io.Serializable

data class MoneyTransferHistoryEntityItem(
    val transactionId: Int? = null,
    val fromCardId: Int? = null,
    val toCardId: Int? = null,
    val fromCardPanMasked: String? = null,
    val toCardPanMasked: String? = null,
    val operationDate: String? = null,
    val amount: Double? = null,
    val currency: String? = null,
    val isIncome: Boolean? = null,
) : Serializable