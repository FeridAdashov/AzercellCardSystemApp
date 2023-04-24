package com.azercell.cardsystem.domain.entity


class UserEntity(
    code: Int,
    message: String? = null,
    val user: UserEntityItem? = null
) : BaseEntity(code, message)