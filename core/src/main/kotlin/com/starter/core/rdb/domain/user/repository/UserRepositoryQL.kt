package com.starter.core.rdb.domain.user.repository

interface UserRepositoryQL {
    fun findAllByName(name: String): List<User>
}
