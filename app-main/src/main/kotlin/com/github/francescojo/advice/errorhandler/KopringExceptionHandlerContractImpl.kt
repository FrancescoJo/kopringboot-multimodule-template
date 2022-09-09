/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.advice.errorhandler

import com.github.francescojo.core.exception.ExternalException
import com.github.francescojo.core.exception.InternalException
import com.github.francescojo.core.exception.KopringException
import org.slf4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest

/**
 * @since 2021-08-10
 */
@Component
internal class KopringExceptionHandlerContractImpl(
    private val log: Logger
) : ExceptionHandlerContract<KopringException>, ErrorCodesToHttpStatusMixin {
    override fun onException(
        req: HttpServletRequest,
        exception: KopringException
    ): Pair<KopringException, HttpStatus>? = when (exception) {
        is ExternalException -> onExternalException(exception)
        is InternalException -> onInternalException(exception)
    }

    private fun onExternalException(exception: ExternalException): Pair<KopringException, HttpStatus> {
        log.debug("Client exception:", exception)
        return exception to exception.toHttpStatus()
    }

    private fun onInternalException(exception: InternalException): Pair<KopringException, HttpStatus> {
        log.debug("Server exception:", exception)
        return exception to exception.toHttpStatus()
    }
}
