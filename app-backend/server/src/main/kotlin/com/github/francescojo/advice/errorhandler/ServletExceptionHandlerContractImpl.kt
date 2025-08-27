/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.advice.errorhandler

import com.github.francescojo.core.exception.KopringException
import com.github.francescojo.core.exception.external.MalformedInputException
import com.github.francescojo.core.exception.external.RequestedResourceNotFoundException
import com.github.francescojo.core.exception.external.RequestedServiceNotFoundException
import com.github.francescojo.core.exception.external.WrongPresentationRequestException
import com.github.francescojo.lib.webApi.advice.errorHandler.ErrorCodesToHttpStatusMixin
import com.github.francescojo.lib.webApi.advice.errorHandler.ExceptionHandlerContract
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.multipart.support.MissingServletRequestPartException
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.resource.NoResourceFoundException

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
        is NoResourceFoundException -> onNoResourceFoundException(exception)
        is MissingServletRequestPartException -> onBadRequestException(exception)
        is HttpMediaTypeNotSupportedException -> onHttpMediaTypeNotSupportedException(exception)
        else -> null
    }?.let { kopringException: KopringException ->
        log.debug("jakarta.servlet.ServletException:", exception)
        return kopringException to kopringException.toHttpStatus()
    }

    // 404
    private fun onNoHandlerFoundException(exception: NoHandlerFoundException): KopringException =
        RequestedServiceNotFoundException(cause = exception)

    // 404
    private fun onNoResourceFoundException(exception: NoResourceFoundException): KopringException =
        RequestedResourceNotFoundException(name = exception.resourcePath, cause = exception)

    // 400
    private fun onBadRequestException(exception: MissingServletRequestPartException): KopringException =
        MalformedInputException(cause = exception)

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
