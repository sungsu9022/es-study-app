package com.starter.core.domain.user.models

import com.starter.core.common.mapper.GenericMapper
import com.starter.core.domain.user.repository.User
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

@Mapper
interface UserMapper : GenericMapper<User, UserVO> {
    companion object {
        val INSTANCE = Mappers.getMapper(UserMapper::class.java)
    }
}
