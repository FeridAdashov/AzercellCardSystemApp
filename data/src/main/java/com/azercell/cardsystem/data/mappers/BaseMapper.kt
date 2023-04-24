package com.azercell.cardsystem.data.mappers

import com.azercell.cardsystem.data.dto.BaseResponse
import com.azercell.cardsystem.domain.entity.BaseEntity

open class BaseMapper {

    fun toBaseEntity(baseResponse: BaseResponse): BaseEntity {
        return BaseEntity(
            code = baseResponse.code,
            message = baseResponse.message,
        )
    }
}