/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.webApi.advice.errorHandler

import com.github.francescojo.core.exception.KopringException
import com.github.francescojo.core.exception.external.MalformedInputException
import com.github.francescojo.core.exception.external.WrongInputException
import com.github.francescojo.core.exception.internal.UnhandledException
import com.github.francescojo.lib.springWebMvc.util.toHttpStatus
import com.github.francescojo.lib.webApi.exception.GeneralHttpException
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolationException
import org.slf4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageConversionException
import org.springframework.stereotype.Component
import org.springframework.validation.BindException
import org.springframework.web.client.HttpStatusCodeException

/**
 * Handles the dispatching of exceptions to appropriate handlers.
 *
 * @since 2021-08-10
 */
@Component
class ExceptionDispatcher(
    private val kopringExceptionHandler: ExceptionHandlerContract<KopringException>,
    private val servletExceptionHandler: ExceptionHandlerContract<ServletException>,
    private val log: Logger
) {
    fun dispatch(
        req: HttpServletRequest,
        exception: Exception
    ): Pair<KopringException, HttpStatus> {
        return dispatchToSpecificHandler(req, exception) ?: handleGenericException(exception)
    }

    private fun dispatchToSpecificHandler(
        req: HttpServletRequest,
        exception: Exception
    ): Pair<KopringException, HttpStatus>? =
        when (exception) {
            is KopringException -> kopringExceptionHandler.onException(req, exception)

            is BindException -> kopringExceptionHandler.onException(
                req, WrongInputException(
                    value = exception.fieldErrors.map { it.field }.toSortedSet().joinToString { it },
                    cause = exception
                )
            )

            is HttpMessageConversionException -> kopringExceptionHandler.onException(
                req, MalformedInputException(cause = exception)
            )

            is ServletException -> servletExceptionHandler.onException(req, exception)

            is HttpStatusCodeException -> GeneralHttpException(
                exception.statusCode,
                cause = exception
            ) to exception.statusCode.toHttpStatus()

            is ConstraintViolationException -> {
                val (value, message) = when (exception.constraintViolations.size) {
                    0 -> "" to "Empty input value"
                    1 -> exception.constraintViolations.first().let {
                        it.invalidValue to "${it.propertyPath} : ${it.message}"
                    }
                    else -> exception.constraintViolations.joinToString {
                        "${it.propertyPath} : ${it.invalidValue}"
                    } to exception.constraintViolations.joinToString {
                        "${it.propertyPath} : ${it.message}"
                    }
                }

                kopringExceptionHandler.onException(
                    req, WrongInputException(
                        value = value,
                        message = message,
                        cause = exception
                    )
                )
            }

            else -> null
        }

    private fun handleGenericException(exception: Exception): Pair<KopringException, HttpStatus> {
        log.error("Unhandled exception:", exception)
        return UnhandledException(cause = exception) to HttpStatus.INTERNAL_SERVER_ERROR
    }
}