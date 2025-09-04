/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.webApi.advice.errorHandler

import com.github.francescojo.core.exception.ErrorCodes
import com.github.francescojo.core.exception.ErrorCodes.*
import com.github.francescojo.core.exception.KopringException
import com.github.francescojo.lib.springWebMvc.util.toHttpStatus
import com.github.francescojo.lib.webApi.exception.GeneralHttpException
import org.springframework.http.HttpStatus

/**
 * @since 2021-08-10
 */
interface ErrorCodesToHttpStatusMixin {
    fun KopringException.toHttpStatus(): HttpStatus = if (this is GeneralHttpException) {
        this.statusCode.toHttpStatus()
    } else {
        if (this.errorCode is ErrorCodes) {
            (this.errorCode as ErrorCodes).toHttpStatus()
        } else {
            HttpStatus.INTERNAL_SERVER_ERROR
        }
    }

    private fun ErrorCodes.toHttpStatus(): HttpStatus = when (this) {
        // General error cases
        SERVICE_NOT_FOUND, RESOURCE_NOT_FOUND -> HttpStatus.NOT_FOUND
        WRONG_PRESENTATION -> HttpStatus.UNSUPPORTED_MEDIA_TYPE
        WRONG_INPUT, MALFORMED_INPUT -> HttpStatus.BAD_REQUEST
        EXCEPTION_ON_TRANSMISSION -> HttpStatus.BAD_REQUEST

        // Domain error cases to HTTP status
        USER_BY_ID_NOT_FOUND -> HttpStatus.NOT_FOUND
        USER_BY_EMAIL_DUPLICATED, USER_BY_NICKNAME_DUPLICATED -> HttpStatus.CONFLICT

        DATA_INTEGRITY_BROKEN,
        UNHANDLED_EXCEPTION -> HttpStatus.INTERNAL_SERVER_ERROR
    }
}
