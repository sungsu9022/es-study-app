package com.starter.core.domain.user.repository

import com.starter.core.common.entity.BaseEntity
import com.starter.core.domain.user.models.UserInterface
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table


@Entity
@Table(name = "user")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long = 0,
    @Column(columnDefinition = "CHAR(36)")
    override val uuid: String,
    override val userName: String,
    override val email: String,
    @Column(name = "is_enabled", columnDefinition = "TINYINT(1)")
    override val enabled: Boolean = true,
    @Column(name = "is_deleted", columnDefinition = "TINYINT(1)")
    override val deleted: Boolean = false,
) : BaseEntity(), UserInterface
