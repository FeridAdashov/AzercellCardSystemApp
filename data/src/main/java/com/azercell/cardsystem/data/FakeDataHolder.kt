package com.azercell.cardsystem.data

import android.annotation.SuppressLint
import com.azercell.cardsystem.data.dto.*
import java.net.HttpURLConnection
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random


object FakeDataHolder {
    @SuppressLint("SimpleDateFormat")
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:SS")

    private lateinit var user: UserResponseItem
    private val cards = mutableListOf<CardsResponseItem>()
    private val randomImages = listOf("") //You can set images here

    private val cardBalanceHolder = mutableListOf<CardBalance>()
    private val moneyTransferHistory = mutableListOf<MoneyTransferResponseItem>()

    fun setUser(
        name: String,
        surname: String,
        birthday: String,
        mobile: String,
    ): BaseResponse {
        user = UserResponseItem(name, surname, birthday, mobile)
        return BaseResponse(code = HttpURLConnection.HTTP_OK, message = "Successfully created!")
    }

    fun getUser(): UserResponse {
        return UserResponse(
            body = UserResponseItem(
                user.name,
                user.surname,
                user.birthday,
                user.mobile
            )
        )
    }

    private fun findBalance(cardId: Int): Double {
        return cardBalanceHolder.first { it.id == cardId }.balance
    }

    fun getCards(): CardsResponse {
        cards.forEachIndexed { index, card ->
            cards[index].balance = card.id?.let { findBalance(it) } ?: 0.0
        }
        return CardsResponse(body = cards)
    }

    fun addCard(pan16: String, expireDate: String, cvv: String, cardName: String?): BaseResponse {
        var mask = ""
        var pan = pan16.replace(" ", "")
        if (pan.length == 16) {
            pan = "${pan.substring(0, 4)} ${pan.substring(4, 8)} " +
                    "${pan.substring(8, 12)} ${pan.substring(12)}"

            pan.toCharArray().forEachIndexed { index, ch ->

                mask += if (ch.isDigit()) {
                    if (index in 8..14)
                        "*"
                    else ch
                } else {
                    ch
                }
            }
        }

        val image = randomImages[Random.nextInt(0, randomImages.size)]
        val cardId = Random.nextInt(0, Int.MAX_VALUE)

        cards.add(
            CardsResponseItem(
                cardId,
                pan,
                mask,
                expireDate,
                cvv,
                cardName,
                image
            )
        )
        cardBalanceHolder.add(CardBalance(cardId, 0.0))

        return BaseResponse(code = HttpURLConnection.HTTP_OK, message = "Card added successfully!")
    }

    fun moneyTransfer(
        fromCardId: Int,
        toCardId: Int,
        amount: Double,
        currency: String
    ): BaseResponse {
        moneyTransferHistory.add(
            MoneyTransferResponseItem(
                Random.nextInt(),
                fromCardId,
                toCardId,
                cards.find { it.id == fromCardId }?.panMasked ?: "",
                cards.find { it.id == toCardId }?.panMasked ?: "",
                simpleDateFormat.format(Date()),
                amount,
                currency
            )
        )
        val indexFromCard = cardBalanceHolder.indexOfFirst { it.id == fromCardId }
        val indexToCard = cardBalanceHolder.indexOfFirst { it.id == toCardId }

        if (indexFromCard > -1) {
            cardBalanceHolder[indexFromCard].balance -= amount
        }
        if (indexFromCard > -1) {
            cardBalanceHolder[indexToCard].balance += amount
        }

        return BaseResponse(code = HttpURLConnection.HTTP_OK, message = "Card added successfully!")
    }

    fun getMoneyTransferHistory(cardId: Int): MoneyTransferHistoryResponse {
        return MoneyTransferHistoryResponse(body = moneyTransferHistory
            .filter { it.fromCardId == cardId || it.toCardId == cardId }
            .map { it.apply { it.isIncome = it.toCardId == cardId } })
    }

    fun increaseCardBalance(cardId: Int, amount: Double): IncreaseCardBalanceResponse {
        val index = cardBalanceHolder.indexOfFirst { it.id == cardId }
        return if (index >= 0) {
            cardBalanceHolder[index].balance += amount
            IncreaseCardBalanceResponse(IncreaseCardBalanceResponseItem(balance = cardBalanceHolder[index].balance))
        } else {
            IncreaseCardBalanceResponse().apply {
                code = HttpURLConnection.HTTP_NOT_FOUND
                message = "Card not found with cardId: $cardId"
            }
        }
    }
}