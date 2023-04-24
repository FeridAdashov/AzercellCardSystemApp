package com.azercell.cardsystem.data.repositories

import com.azercell.cardsystem.data.ExceptionUtils
import com.azercell.cardsystem.data.api.AppServices
import com.azercell.cardsystem.data.dto.BaseResponse
import com.azercell.cardsystem.data.dto.CardsResponse
import com.azercell.cardsystem.data.mappers.CardsMapper
import com.azercell.cardsystem.domain.entity.BaseEntity
import com.azercell.cardsystem.domain.entity.CardsEntity
import com.azercell.cardsystem.domain.repositories.CardsRepository
import com.google.gson.Gson

class CardsRepositoryImpl(
    private val apiService: AppServices,
    private val cardsMapper: CardsMapper,
) : CardsRepository {

    override suspend fun getAllCards(): CardsEntity {
        return try {
            val response = apiService.getAllCards()

            if (response.isSuccessful) {
                cardsMapper.toCardsEntity(response.body()!!)
            } else {
                val errorResponse = Gson().fromJson(
                    response.errorBody()?.string(),
                    CardsResponse::class.java
                )
                cardsMapper.toCardsEntity(errorResponse)
            }
        } catch (e: Exception) {
            return CardsEntity(
                code = ExceptionUtils.getExceptionCode(e),
                message = e.message
            )
        }
    }

    override suspend fun addCard(pan16: String, expireDate: String, cvv: String, cardName: String?): BaseEntity {
        return try {
            val response = apiService.addCard(pan16, expireDate, cvv, cardName)

            if (response.isSuccessful) {
                cardsMapper.toBaseEntity(response.body()!!)
            } else {
                val errorResponse = Gson().fromJson(
                    response.errorBody()?.string(),
                    BaseResponse::class.java
                )
                cardsMapper.toBaseEntity(errorResponse)
            }
        } catch (e: Exception) {
            return BaseEntity(
                code = ExceptionUtils.getExceptionCode(e),
                message = e.message
            )
        }
    }
}