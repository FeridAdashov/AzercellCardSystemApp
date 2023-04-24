package com.azercell.cardsystem.data.mappers

import com.azercell.cardsystem.data.dto.UserResponse
import com.azercell.cardsystem.data.dto.UserResponseItem
import com.azercell.cardsystem.domain.entity.UserEntity
import com.azercell.cardsystem.domain.entity.UserEntityItem

class UserMapper : BaseMapper() {

    fun toUserEntity(it: UserResponse): UserEntity {
        return UserEntity(
            code = it.code,
            message = it.message,
            user = toUserEntityItem(it.body)
        )
    }

    fun toUserEntityItem(it: UserResponseItem?): UserEntityItem {
        return UserEntityItem(
            name = it?.name ?: "",
            surname = it?.surname ?: "",
            birthday = it?.birthday ?: "",
            mobile = it?.mobile ?: "",
        )
    }
}