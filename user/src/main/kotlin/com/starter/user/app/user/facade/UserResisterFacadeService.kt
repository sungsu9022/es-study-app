package com.starter.user.app.user.facade

import com.starter.core.common.utils.UUIDUtils
import com.starter.core.rdb.domain.user.models.UserVO
import com.starter.user.app.user.mapper.UserModelMapper
import com.starter.user.app.user.model.UserCreateRequest
import com.starter.user.app.user.service.UserService
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserResisterFacadeService(
    private val userService: UserService,
    private val userModelMapper: UserModelMapper,
) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    @Transactional
    fun resisterMember(userCreateRequest: UserCreateRequest): UserVO {
        val uuid = UUIDUtils.generate()
        val user = userModelMapper.convertToEntity(userCreateRequest, uuid)
        val saved = userService.save(user)
        logger.debug { "saved : $saved" }
        return saved
    }
}
