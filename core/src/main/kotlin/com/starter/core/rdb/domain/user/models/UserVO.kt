package com.starter.core.rdb.domain.user.models

import java.time.LocalDateTime


data class UserVO(
    override val id: Long,
    override val uuid: String,
    override val userName: String,
    override val email: String,
    override val enabled: Boolean,
    override val deleted: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) : UserInterface
