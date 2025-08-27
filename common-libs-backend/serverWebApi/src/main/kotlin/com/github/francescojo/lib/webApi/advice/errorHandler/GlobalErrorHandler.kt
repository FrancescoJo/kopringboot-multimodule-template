package com.github.francescojo.lib.webApi.advice.errorHandler

import com.github.francescojo.core.exception.ErrorCodes
import com.github.francescojo.core.exception.InternalException
import com.github.francescojo.lib.springWebMvc.util.originalRequestUri
import com.github.francescojo.lib.springWebMvc.util.toHttpStatus
import com.github.francescojo.lib.webApi.response.base.ErrorResponseEnvelope
import jakarta.servlet.RequestDispatcher
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * This class overrides the default error-handling mechanism of Spring-web.
 * It delegates exception handling to ExceptionDispatcher and response wrapping to ErrorResponseWrapper.
 *
 * @since 2021-08-10
 */
@RestController
@RestControllerAdvice
class GlobalErrorHandler(
    private val exceptionDispatcher: ExceptionDispatcher,
    private val responseWrapper: ErrorResponseWrapper,
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

        return responseWrapper.wrapError(InternalException(ErrorCodes.UNHANDLED_EXCEPTION), httpStatus)
    }

    @ExceptionHandler(Exception::class)
    fun onError(
        req: HttpServletRequest,
        exception: Exception
    ): ResponseEntity<ErrorResponseEnvelope> {
        val (kopringException, httpStatus) = exceptionDispatcher.dispatch(req, exception)

        return responseWrapper.wrapError(kopringException, httpStatus)
    }

    companion object {
        private const val SERVLET_EXCEPTION = RequestDispatcher.ERROR_EXCEPTION
    }
}
