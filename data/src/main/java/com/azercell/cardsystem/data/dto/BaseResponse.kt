package com.azercell.cardsystem.data.dto

import com.google.gson.annotations.SerializedName
import java.net.HttpURLConnection

open class BaseResponse(
    @field:SerializedName("message")
    var message: String? = null,

    @field:SerializedName("code")
    var code: Int = HttpURLConnection.HTTP_OK,
)