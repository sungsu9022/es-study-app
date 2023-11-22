package com.starter.core.domain.user.repository

interface UserRepositoryQL {
    fun findAllByName(name: String): List<User>
}
