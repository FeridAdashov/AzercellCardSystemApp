package com.azercell.cardsystem.data.api

import com.azercell.cardsystem.data.FakeDataHolder
import com.azercell.cardsystem.data.dto.*
import retrofit2.Response


class AppServicesImpl : AppServices {
    override suspend fun createUser(
        name: String,
        surname: String,
        birthday: String,
        mobile: String
    ): Response<BaseResponse> {
        return Response.success(
            FakeDataHolder.setUser(name, surname, birthday, mobile)
        )
    }

    override suspend fun getUser(): Response<UserResponse> {
        return Response.success(FakeDataHolder.getUser())
    }

    override suspend fun getAllCards(): Response<CardsResponse> {
        return Response.success(FakeDataHolder.getCards())
    }

    override suspend fun addCard(
        pan16: String,
        expireDate: String,
        cvv: String,
        cardName: String?
    ): Response<BaseResponse> {
        return Response.success(
            FakeDataHolder.addCard(pan16, expireDate, cvv, cardName)
        )
    }

    override suspend fun moneyTransfer(
        fromCardId: Int,
        toCardId: Int,
        amount: Double,
        currency: String
    ): Response<BaseResponse> {
        return Response.success(
            FakeDataHolder.moneyTransfer(fromCardId, toCardId, amount, currency)
        )
    }

    override suspend fun getMoneyTransferHistory(cardId: Int): Response<MoneyTransferHistoryResponse> {
        return Response.success(FakeDataHolder.getMoneyTransferHistory(cardId))
    }

    override suspend fun increaseCardBalance(cardId: Int, amount: Double): Response<IncreaseCardBalanceResponse> {
        return Response.success(FakeDataHolder.increaseCardBalance(cardId, amount))
    }
}