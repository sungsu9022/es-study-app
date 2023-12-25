package com.starter.user.app.user.mapper

import com.starter.core.rdb.domain.user.repository.User
import com.starter.user.app.user.model.UserCreateRequest
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

@Mapper
interface UserModelMapper  {
    companion object {
        val INSTANCE = Mappers.getMapper(UserModelMapper::class.java)
    }

    fun convertToEntity(
        userCreateRequest: UserCreateRequest,
        uuid: String
    ): User {
        return User(
            userName = userCreateRequest.userName,
            email = userCreateRequest.email,
            uuid = uuid,
        )
    }


}

