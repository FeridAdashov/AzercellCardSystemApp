package com.azercell.cardsystem.data.repositories

import com.azercell.cardsystem.data.ExceptionUtils
import com.azercell.cardsystem.data.api.AppServices
import com.azercell.cardsystem.data.dto.BaseResponse
import com.azercell.cardsystem.data.dto.UserResponse
import com.azercell.cardsystem.data.mappers.UserMapper
import com.azercell.cardsystem.domain.entity.BaseEntity
import com.azercell.cardsystem.domain.entity.UserEntity
import com.azercell.cardsystem.domain.repositories.UserRepository
import com.google.gson.Gson

class UserRepositoryImpl(
    private val apiService: AppServices,
    private val userMapper: UserMapper,
) : UserRepository {

    override suspend fun createUser(
        name: String,
        surname: String,
        birthday: String,
        mobile: String
    ): BaseEntity {
        return try {
            val response = apiService.createUser(name, surname, birthday, mobile)

            if (response.isSuccessful) {
                userMapper.toBaseEntity(response.body()!!)
            } else {
                val errorResponse = Gson().fromJson(
                    response.errorBody()?.string(),
                    BaseResponse::class.java
                )
                userMapper.toBaseEntity(errorResponse)
            }
        } catch (e: Exception) {
            return UserEntity(
                code = ExceptionUtils.getExceptionCode(e),
                message = e.message
            )
        }
    }

    override suspend fun getUser(): UserEntity {
        return try {
            val response = apiService.getUser()

            if (response.isSuccessful) {
                userMapper.toUserEntity(response.body()!!)
            } else {
                val errorResponse = Gson().fromJson(
                    response.errorBody()?.string(),
                    UserResponse::class.java
                )
                userMapper.toUserEntity(errorResponse)
            }
        } catch (e: Exception) {
            return UserEntity(
                code = ExceptionUtils.getExceptionCode(e),
                message = e.message
            )
        }
    }

}