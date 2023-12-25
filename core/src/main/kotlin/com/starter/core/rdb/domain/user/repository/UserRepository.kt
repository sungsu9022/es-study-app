package com.starter.core.rdb.domain.user.repository

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long>, UserRepositoryQL {

    fun findByUuid(uuid: String): User?

}
