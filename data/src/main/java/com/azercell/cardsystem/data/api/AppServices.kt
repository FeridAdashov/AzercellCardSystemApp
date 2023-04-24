package com.azercell.cardsystem.data.api

import com.azercell.cardsystem.data.dto.*
import retrofit2.Response
import retrofit2.http.*


interface AppServices {

    @GET("v1/users/")
    suspend fun createUser(
        name: String,
        surname: String,
        birthday: String,
        mobile: String
    ): Response<BaseResponse>

    @POST("v1/users/")
    suspend fun getUser(): Response<UserResponse>

    @GET("v1/cards/")
    suspend fun getAllCards(): Response<CardsResponse>

    @POST("v1/cards/")
    suspend fun addCard(
        pan16: String, expireDate: String, cvv: String, cardName: String?
    ): Response<BaseResponse>

    @POST("v1/moneyTransfer/")
    suspend fun moneyTransfer(
        fromCardId: Int,
        toCardId: Int,
        amount: Double,
        currency: String
    ): Response<BaseResponse>

    @GET("v1/moneyTransfer/")
    suspend fun getMoneyTransferHistory(cardId: Int): Response<MoneyTransferHistoryResponse>

    @POST("v1/balance/")
    suspend fun increaseCardBalance(
        cardId: Int,
        amount: Double
    ): Response<IncreaseCardBalanceResponse>
}
