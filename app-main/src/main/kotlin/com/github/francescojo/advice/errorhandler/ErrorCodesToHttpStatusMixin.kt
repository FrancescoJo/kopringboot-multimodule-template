/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.advice.errorhandler

import com.github.francescojo.core.exception.ErrorCodes
import com.github.francescojo.core.exception.ErrorCodes.*
import com.github.francescojo.core.exception.KopringException
import com.github.francescojo.exception.GeneralHttpException
import org.springframework.http.HttpStatus

/**
 * @since 2021-08-10
 */
interface ErrorCodesToHttpStatusMixin {
    fun KopringException.toHttpStatus() = if (this is GeneralHttpException) {
        this.statusCode
    } else {
        this.codeBook.toHttpStatus()
    }

    fun ErrorCodes.toHttpStatus(): HttpStatus = when (this) {
        // General error cases
        SERVICE_NOT_FOUND -> HttpStatus.NOT_FOUND
        WRONG_PRESENTATION -> HttpStatus.UNSUPPORTED_MEDIA_TYPE
        WRONG_INPUT, MALFORMED_INPUT -> HttpStatus.BAD_REQUEST
        GENERAL_HTTP_EXCEPTION -> HttpStatus.BAD_REQUEST

        // Domain error cases to HTTP status
        USER_BY_ID_NOT_FOUND -> HttpStatus.NOT_FOUND
        USER_BY_EMAIL_DUPLICATED, USER_BY_NICKNAME_DUPLICATED -> HttpStatus.CONFLICT

        UNHANDLED_EXCEPTION -> HttpStatus.INTERNAL_SERVER_ERROR
    }
}
