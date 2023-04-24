package com.azercell.cardsystem.domain.repositories

import com.azercell.cardsystem.domain.entity.BaseEntity
import com.azercell.cardsystem.domain.entity.UserEntity

interface UserRepository {

    /**
     * Get user
     */
    suspend fun getUser(): UserEntity

    /**
     * Create user
     */
    suspend fun createUser(
        name: String,
        surname: String,
        birthday: String,
        mobile: String
    ): BaseEntity

}