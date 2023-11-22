package com.starter.core.domain.user.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.starter.core.domain.user.repository.QUser.user
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class UserRepositoryQLImpl(
    private val jpaQueryFactory: JPAQueryFactory,
) : UserRepositoryQL, QuerydslRepositorySupport(User::class.java) {
    override fun findAllByName(userName: String): List<User> {
        return jpaQueryFactory
            .selectFrom(user)
            .where(user.userName.eq(userName))
            .fetch()
    }
}
