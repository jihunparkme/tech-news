package com.technews.common.dto

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity

class BasicResponse<T>(
    val status: Int = 0,
    val httpStatusCode: HttpStatusCode? = null,
    val success: Boolean = false,
    val message: String? = null,
    val count: Int? = 0,
    val data: T? = null,
) {
    companion object {
        fun internalServerError(message: String): ResponseEntity<BasicResponse<Unit>> =
            buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, message)

        fun clientError(message: String): ResponseEntity<BasicResponse<Unit>> =
            buildResponse(HttpStatus.BAD_REQUEST, message)

        fun notFound(message: String): ResponseEntity<BasicResponse<Unit>> =
            buildResponse(HttpStatus.NOT_FOUND, message)

        fun ok(success: Boolean): ResponseEntity<BasicResponse<Boolean>> =
            buildResponse(HttpStatus.OK, success = true, data = success)

        fun <T> ok(data: T): ResponseEntity<BasicResponse<T>> =
            buildResponse(HttpStatus.OK, success = true, data = data, count = 1)

        fun <T> ok(data: List<T>): ResponseEntity<BasicResponse<List<T>>> =
            buildResponse(HttpStatus.OK, success = true, data = data, count = data.size)

        fun <T> created(data: T): ResponseEntity<BasicResponse<T>> =
            buildResponse(HttpStatus.CREATED, success = true, data = data, count = 1)

        fun <T> created(data: List<T>): ResponseEntity<BasicResponse<List<T>>> =
            buildResponse(HttpStatus.CREATED, success = true, data = data, count = data.size)

        private fun <T> buildResponse(
            status: HttpStatus,
            message: String? = null,
            success: Boolean = false,
            data: T? = null,
            count: Int? = null,
        ): ResponseEntity<BasicResponse<T>> {
            val basicResponse = BasicResponse(
                status = status.value(),
                httpStatusCode = status,
                success = success,
                message = message,
                count = count,
                data = data,
            )
            return ResponseEntity(basicResponse, status)
        }
    }
}
