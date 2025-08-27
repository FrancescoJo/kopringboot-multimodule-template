/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.advice.errorhandler

import com.github.francescojo.core.exception.KopringException
import com.github.francescojo.core.exception.external.WrongInputException
import com.github.francescojo.core.exception.internal.UnhandledException
import com.github.francescojo.lib.webApi.advice.errorHandler.ErrorCodesToHttpStatusMixin
import com.github.francescojo.lib.webApi.advice.errorHandler.ExceptionHandlerContract
import com.github.francescojo.lib.webApi.exception.GeneralHttpException
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.springframework.core.NestedRuntimeException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.multipart.MaxUploadSizeExceededException
import org.springframework.web.multipart.MultipartException

/**
 * Handles some exceptions from Spring Web that extend Spring Core's [NestedRuntimeException].
 *
 * Caution: Do not implement this handler to handle all [NestedRuntimeException]s. Because most [NestedRuntimeException]s
 * should be handled as Internal Errors, due to their nature.
 *
 * @since 2021-08-10
 */
@Component
internal class SpringExceptionHandlerContractImpl(
    private val log: Logger
) : ExceptionHandlerContract<NestedRuntimeException>, ErrorCodesToHttpStatusMixin {
    override fun onException(
        req: HttpServletRequest,
        exception: NestedRuntimeException
    ): Pair<KopringException, HttpStatus> = when (exception) {
        is MaxUploadSizeExceededException -> onBadRequestException(exception)
        is MultipartException -> onBadRequestException(exception)
        is HttpStatusCodeException -> GeneralHttpException(exception.statusCode, cause = exception)
        else -> UnhandledException(cause = exception)
    }.let { e: KopringException ->
        log.debug("org.springframework.core.NestedRuntimeException:", exception)
        return e to e.toHttpStatus()
    }

    // 400
    private fun onBadRequestException(exception: MaxUploadSizeExceededException): KopringException =
        WrongInputException(
            value = "",
            message = "Payload too large: ${exception.maxUploadSize} bytes",
            cause = exception
        )

    private fun onBadRequestException(exception: MultipartException): KopringException =
        WrongInputException(
            value = "",
            message = "Error in multipart request ingestion",
            cause = exception
        )
}
