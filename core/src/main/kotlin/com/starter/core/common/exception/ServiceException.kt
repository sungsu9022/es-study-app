package com.starter.core.common.exception

class ServiceException(
    val errorCode: ErrorCode,
    override val message: String
) : RuntimeException(message) {
    constructor(errorCode: ErrorCode) : this(errorCode, "")
}
