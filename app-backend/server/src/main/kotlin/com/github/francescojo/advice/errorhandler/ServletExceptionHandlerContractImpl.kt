/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.advice.errorhandler

import com.github.francescojo.core.exception.KopringException
import com.github.francescojo.core.exception.external.RequestedServiceNotFoundException
import com.github.francescojo.core.exception.external.WrongPresentationRequestException
import com.github.francescojo.lib.webApi.advice.ErrorCodesToHttpStatusMixin
import com.github.francescojo.lib.webApi.advice.ExceptionHandlerContract
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.servlet.NoHandlerFoundException

/**
 * @since 2021-08-10
 */
@Component
internal class ServletExceptionHandlerContractImpl(
    private val log: Logger
) : ExceptionHandlerContract<ServletException>, ErrorCodesToHttpStatusMixin {
    override fun onException(
        req: HttpServletRequest,
        exception: ServletException
    ): Pair<KopringException, HttpStatus>? = when (exception) {
        is HttpRequestMethodNotSupportedException -> onHttpRequestMethodNotSupportedException(exception)
        is NoHandlerFoundException -> onNoHandlerFoundException(exception)
        is HttpMediaTypeNotSupportedException -> onHttpMediaTypeNotSupportedException(exception)
        else -> null
    }?.let { kopringException: KopringException ->
        log.debug("jakarta.servlet.ServletException:", exception)
        return kopringException to kopringException.toHttpStatus()
    }

    // 404
    private fun onNoHandlerFoundException(exception: NoHandlerFoundException): KopringException =
        RequestedServiceNotFoundException(cause = exception)

    // 405 (Originally)
    private fun onHttpRequestMethodNotSupportedException(
        exception: HttpRequestMethodNotSupportedException
    ): KopringException =
        RequestedServiceNotFoundException(cause = exception)

    // 415
    private fun onHttpMediaTypeNotSupportedException(
        exception: HttpMediaTypeNotSupportedException
    ): KopringException =
        WrongPresentationRequestException(cause = exception)
}
