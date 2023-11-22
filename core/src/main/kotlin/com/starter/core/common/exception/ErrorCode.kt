package com.starter.core.common.exception

import com.starter.core.common.response.ResponseCode
import org.springframework.boot.logging.LogLevel

enum class ErrorCode(val resposneCode: ResponseCode, val logLevel: LogLevel) {
    INVALID_PARAMETER(ResponseCode.INVALID_PARAMETER, logLevel = LogLevel.DEBUG),
    DUPLICATED(ResponseCode.DUPLICATED, logLevel = LogLevel.DEBUG),
    NOT_FOUND(ResponseCode.NOT_FOUND, logLevel = LogLevel.DEBUG),
    UNSUPPORT(ResponseCode.UNSUPPORT, logLevel = LogLevel.ERROR),
    UNKNOWN(ResponseCode.UNKNOWN, logLevel = LogLevel.ERROR),
}
