package com.starter.user.app.user.facade

import com.starter.core.domain.user.models.UserVO
import com.starter.user.app.user.service.UserService
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class UserReadFacadeService(
    private val userService: UserService,
) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    fun getUser(uuid: String): UserVO {
        return userService.getByUuid(uuid)
    }

    fun getUsers(name: String): List<UserVO> {
        return userService.getUsersByName(name)
    }
}
