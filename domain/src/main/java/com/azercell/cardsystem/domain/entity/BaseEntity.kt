package com.azercell.cardsystem.domain.entity

import java.net.HttpURLConnection

open class BaseEntity(
    val code: Int = HttpURLConnection.HTTP_OK,
    val message: String? = null,
)
