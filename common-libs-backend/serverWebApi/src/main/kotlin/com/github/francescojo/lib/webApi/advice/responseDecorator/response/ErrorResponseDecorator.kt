/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.webApi.advice.responseDecorator.response

import com.github.francescojo.core.exception.ErrorCodes
import com.github.francescojo.core.exception.InternalException
import com.github.francescojo.core.exception.KopringException
import com.github.francescojo.core.exception.external.MalformedInputException
import com.github.francescojo.core.exception.external.WrongInputException
import com.github.francescojo.core.exception.internal.UnhandledException
import com.github.francescojo.core.i18n.MessageTemplateProvider
import com.github.francescojo.lib.i18n.LocaleProvider
import com.github.francescojo.lib.springWebMvc.util.originalRequestUri
import com.github.francescojo.lib.springWebMvc.util.toHttpStatus
import com.github.francescojo.lib.webApi.advice.ExceptionHandlerContract
import com.github.francescojo.lib.webApi.exception.GeneralHttpException
import com.github.francescojo.lib.webApi.response.base.ErrorResponseEnvelope
import com.github.francescojo.lib.webApi.response.base.ResponseEnvelope
import jakarta.servlet.RequestDispatcher
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolationException
import org.slf4j.Logger
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageConversionException
import org.springframework.validation.BindException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.client.HttpStatusCodeException

/**
 * This class overrides the default error-handling mechanism of Spring-web.
 *
 * @since 2021-08-10
 */
@RestController
@RestControllerAdvice
class ErrorResponseDecorator(
    private val kopringExceptionHandler: ExceptionHandlerContract<KopringException>,
    private val servletExceptionHandler: ExceptionHandlerContract<ServletException>,
    private val localeProvider: LocaleProvider,
    private val messageTemplateProvider: MessageTemplateProvider,
    private val log: Logger
) : ErrorController {
    /**
     * This method assumes that all modules using this library, have a common error-handling mechanism
     * via [Spring BasicErrorController](https://github.com/spring-projects/spring-boot/blob/main/spring-boot-project/spring-boot-autoconfigure/src/main/java/org/springframework/boot/autoconfigure/web/servlet/error/BasicErrorController.java#L58)
     * and not modified the default error endpoint which could be set by `server.error.path` property.
     */
    @RequestMapping("/error")
    fun handleError(req: HttpServletRequest): ResponseEntity<ErrorResponseEnvelope> {
        (req.getAttribute(SERVLET_EXCEPTION) as? Exception)?.let { return onError(req, it) }

        val httpStatus = req.toHttpStatus()

        if (log.isErrorEnabled) {
            log.error("Unhandled servlet exception: HTTP {} : {}", httpStatus.value(), req.originalRequestUri())
        }

        return postProcessError(InternalException(ErrorCodes.UNHANDLED_EXCEPTION), httpStatus)
    }

    @ExceptionHandler(Exception::class)
    fun onError(
        req: HttpServletRequest,
        exception: Exception
    ): ResponseEntity<ErrorResponseEnvelope> {
        val (httpStatus, mtException) = dispatchExceptions(req, exception) ?: handleGenericException(exception)

        return postProcessError(httpStatus, mtException)
    }

    private fun dispatchExceptions(
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

    private fun postProcessError(
        exception: KopringException?,
        status: HttpStatus
    ): ResponseEntity<ErrorResponseEnvelope> {
        val message = if (exception == null) {
            DEFAULT_ERROR_MESSAGE
        } else {
            val clientLocale = localeProvider.locale
            val messageTemplate = messageTemplateProvider.provide(clientLocale, exception.codeBook.asMessageKey)

            exception.messageArguments()?.let {
                @Suppress("SpreadOperator")
                messageTemplate?.format(it)
            } ?: exception.message
        }

        val response = if (exception == null) {
            ResponseEnvelope.error(message, ErrorCodes.UNHANDLED_EXCEPTION.toString(), null)
        } else {
            ResponseEnvelope.error(message, exception.codeBook.code.toString(), exception.details())
        }

        return ResponseEntity(response, status)
    }

    companion object {
        private const val SERVLET_EXCEPTION = RequestDispatcher.ERROR_EXCEPTION

        private const val DEFAULT_ERROR_MESSAGE = "Cannot process this request."
    }
}
