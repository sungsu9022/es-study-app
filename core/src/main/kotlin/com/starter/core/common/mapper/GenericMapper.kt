package com.starter.core.common.mapper

interface GenericMapper<E, T> {
    fun convertToTarget(entity: E): T
    fun convertToEntity(target: T): E
}
