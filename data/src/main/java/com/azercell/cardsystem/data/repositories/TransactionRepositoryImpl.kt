package com.azercell.cardsystem.data.repositories

import com.azercell.cardsystem.data.ExceptionUtils
import com.azercell.cardsystem.data.api.AppServices
import com.azercell.cardsystem.data.dto.BaseResponse
import com.azercell.cardsystem.data.dto.IncreaseCardBalanceResponse
import com.azercell.cardsystem.data.dto.MoneyTransferHistoryResponse
import com.azercell.cardsystem.data.mappers.TransactionMapper
import com.azercell.cardsystem.domain.entity.BaseEntity
import com.azercell.cardsystem.domain.entity.IncreaseCardBalanceEntity
import com.azercell.cardsystem.domain.entity.MoneyTransferHistoryEntity
import com.azercell.cardsystem.domain.repositories.TransactionRepository
import com.google.gson.Gson

class TransactionRepositoryImpl(
    private val apiService: AppServices,
    private val transactionMapper: TransactionMapper,
) : TransactionRepository {


    override suspend fun getMoneyTransferHistory(cardId: Int): MoneyTransferHistoryEntity {
        return try {
            val response = apiService.getMoneyTransferHistory(cardId)

            if (response.isSuccessful) {
                transactionMapper.toMoneyTransferHistoryEntity(response.body()!!)
            } else {
                val errorResponse = Gson().fromJson(
                    response.errorBody()?.string(),
                    MoneyTransferHistoryResponse::class.java
                )
                transactionMapper.toMoneyTransferHistoryEntity(errorResponse)
            }
        } catch (e: Exception) {
            return MoneyTransferHistoryEntity(
                code = ExceptionUtils.getExceptionCode(e),
                message = e.message
            )
        }
    }

    override suspend fun moneyTransfer(
        fromCardId: Int,
        toCardId: Int,
        amount: Double,
        currency: String
    ): BaseEntity {
        return try {
            val response = apiService.moneyTransfer(fromCardId, toCardId, amount, currency)

            if (response.isSuccessful) {
                transactionMapper.toBaseEntity(response.body()!!)
            } else {
                val errorResponse = Gson().fromJson(
                    response.errorBody()?.string(),
                    BaseResponse::class.java
                )
                transactionMapper.toBaseEntity(errorResponse)
            }
        } catch (e: Exception) {
            return MoneyTransferHistoryEntity(
                code = ExceptionUtils.getExceptionCode(e),
                message = e.message
            )
        }
    }

    override suspend fun increaseBalance(cardId: Int, amount: Double): IncreaseCardBalanceEntity {
        return try {
            val response = apiService.increaseCardBalance(cardId, amount)

            if (response.isSuccessful) {
                transactionMapper.toIncreaseCardBalanceEntity(response.body()!!)
            } else {
                val errorResponse = Gson().fromJson(
                    response.errorBody()?.string(),
                    IncreaseCardBalanceResponse::class.java
                )
                transactionMapper.toIncreaseCardBalanceEntity(errorResponse)
            }
        } catch (e: Exception) {
            return IncreaseCardBalanceEntity(
                code = ExceptionUtils.getExceptionCode(e),
                message = e.message
            )
        }
    }
}