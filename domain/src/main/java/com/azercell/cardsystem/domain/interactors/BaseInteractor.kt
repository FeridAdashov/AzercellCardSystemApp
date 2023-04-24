package com.azercell.cardsystem.domain.interactors

import com.azercell.cardsystem.domain.entity.BaseEntity
import com.azercell.cardsystem.domain.entity.RequestResult

open class BaseInteractor {

    fun <T : BaseEntity> generateResult(entity: T): RequestResult<T> {

        /** We can add some logic for success and failure here when data get */
        return if (entity.code in 200..202)
            RequestResult.Success(body = entity, code = entity.code)
        else RequestResult.Error(code = entity.code, message = entity.message ?: "")
    }
}