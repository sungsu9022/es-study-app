package com.starter.core.common.response

import com.fasterxml.jackson.annotation.JsonIgnore

interface ServiceResponse

open class BaseResponse<T>(
    val success: Boolean,
    val status: Status,
    var results: T?
) : ServiceResponse

class SuccessResponse<T : Any?>(status: Status, result: T) : BaseResponse<T>(true, status, result) {
    companion object {
        private const val OK = "ok"
        val DEFAULT = of(Status.DEFAULT, OK)

        fun <T> of(results: T): SuccessResponse<T> {
            return SuccessResponse(Status.DEFAULT, results)
        }

        fun <T> of(status: Status, results: T): SuccessResponse<T> {
            return SuccessResponse(status, results);
        }
    }
}

class FailureResponse(val status: Status) : ServiceResponse {
    companion object {
        fun create(responseCode: ResponseCode, message: String): FailureResponse {
            return FailureResponse(Status(responseCode, message))
        }

        fun create(responseCode: ResponseCode): FailureResponse {
            return FailureResponse(Status(responseCode, ""))
        }
    }

    val success: Boolean = false
}

data class Status(
    @JsonIgnore
    val responseCode: ResponseCode,
    val message: String = ""
) {
    companion object {
        val DEFAULT = Status(ResponseCode.OK)
    }

    val code: Int
        get() = responseCode.code
}
