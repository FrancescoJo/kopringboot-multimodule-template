/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.webApi.advice.errorHandler

import com.github.francescojo.core.exception.ErrorCodes
import com.github.francescojo.core.exception.KopringException
import com.github.francescojo.lib.i18n.LocaleProvider
import com.github.francescojo.lib.webApi.response.base.ErrorResponseEnvelope
import com.github.francescojo.lib.webApi.response.base.ResponseEnvelope
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

/**
 * Handles wrapping error responses into appropriate envelopes.
 *
 * @since 2021-08-10
 */
@Component
class ErrorResponseWrapper(
    private val localeProvider: LocaleProvider,
    private val messageTranslator: ErrorMessageTranslator
) {
    fun wrapError(
        exception: KopringException?,
        status: HttpStatus
    ): ResponseEntity<ErrorResponseEnvelope> {
        val message = if (exception == null) {
            DEFAULT_ERROR_MESSAGE
        } else {
            messageTranslator.translate(localeProvider.locale, exception)
        }

        val response = if (exception == null) {
            ResponseEnvelope.Companion.error(message, ErrorCodes.UNHANDLED_EXCEPTION.toString(), null)
        } else {
            ResponseEnvelope.Companion.error(
                message,
                String.format("0x%08x", exception.errorCode.code),
                exception.details()
            )
        }

        return ResponseEntity(response, status)
    }

    companion object {
        private const val DEFAULT_ERROR_MESSAGE = "Cannot process this request."
    }
}
