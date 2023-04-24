package com.azercell.cardsystem.data

import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException

object ExceptionUtils {
    fun getExceptionCode(e: Exception): Int {
        return when (e) {
            is HttpException -> e.code()
            is ConnectException -> Constants.CONNECTION_EXCEPTION
            is SocketException -> Constants.SOCKET_EXCEPTION
            is SocketTimeoutException -> Constants.SOCKET_TIMEOUT_EXCEPTION
            is java.lang.IllegalStateException, is com.google.gson.JsonSyntaxException -> Constants.WRONG_DATA_EXCEPTION
            else -> Constants.UNKNOWN_ERROR
        }
    }
}