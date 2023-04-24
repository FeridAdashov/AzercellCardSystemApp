package com.azercell.cardsystem.domain.interactors

import com.azercell.cardsystem.domain.entity.BaseEntity
import com.azercell.cardsystem.domain.entity.RequestResult
import com.azercell.cardsystem.domain.entity.UserEntity
import com.azercell.cardsystem.domain.repositories.UserRepository

class UserInteractor(private val repository: UserRepository) : BaseInteractor() {

    suspend fun createUser(
        name: String,
        surname: String,
        birthday: String,
        mobile: String
    ): RequestResult<BaseEntity> {
        return generateResult(repository.createUser(name, surname, birthday, mobile))
    }

    suspend fun getUser(): RequestResult<UserEntity> {
        return generateResult(repository.getUser())
    }

}