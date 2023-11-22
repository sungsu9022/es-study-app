package com.starter.core.common.response

import org.springframework.http.HttpStatus

enum class ResponseCode(val code: Int, val httpStatus: HttpStatus) {
    OK(2000, HttpStatus.OK),
    DUPLICATED(2001, HttpStatus.INTERNAL_SERVER_ERROR),
    NOT_FOUND(4004, HttpStatus.NOT_FOUND)
    /** 4100 번대 Event Validation 실패 응답 **/
    ,
    INVALID_PARAMETER(4100, HttpStatus.BAD_REQUEST),
    ETC(4106, HttpStatus.BAD_REQUEST),
    UNSUPPORT(9998, HttpStatus.NOT_ACCEPTABLE),
    UNKNOWN(9999, HttpStatus.INTERNAL_SERVER_ERROR)
}
