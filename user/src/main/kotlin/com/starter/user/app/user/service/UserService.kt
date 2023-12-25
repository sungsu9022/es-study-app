package com.starter.user.app.user.service

import com.starter.core.common.exception.ErrorCode
import com.starter.core.common.exception.ServiceException
import com.starter.core.rdb.domain.user.models.UserMapper
import com.starter.core.rdb.domain.user.models.UserVO
import com.starter.core.rdb.domain.user.repository.User
import com.starter.core.rdb.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val repository: UserRepository,
    private val mapper: UserMapper
) {

    fun getByUuid(uuid: String): UserVO {
        return repository.findByUuid(uuid)
            ?.let { mapper.convertToTarget(it) }
            ?: throw ServiceException(ErrorCode.NOT_FOUND)
    }

    fun getUsersByName(userName: String): List<UserVO> {
        return repository.findAllByName(userName)
            .map { mapper.convertToTarget(it) }
    }

    @Transactional
    fun save(user: User): UserVO {
        val saved = repository.save(user)
        return mapper.convertToTarget(saved)
    }
}
