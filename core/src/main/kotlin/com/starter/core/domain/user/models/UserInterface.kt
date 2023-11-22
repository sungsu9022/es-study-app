package com.starter.core.domain.user.models

interface UserInterface {
    val id: Long
    val uuid: String
    val userName: String
    val email: String
    val enabled: Boolean
    val deleted: Boolean
}
