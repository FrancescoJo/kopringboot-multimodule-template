/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.exception

import com.github.francescojo.core.exception.ErrorCodes
import com.github.francescojo.core.exception.ExternalException
import org.springframework.http.HttpStatus

/**
 * @since 2021-08-10
 */
class GeneralHttpException(
    val statusCode: HttpStatus,
    override val message: String = "An unhandled HTTP exception(status = ${statusCode.value()} is occurred.",
    override val cause: Throwable? = null
) : ExternalException(ErrorCodes.GENERAL_HTTP_EXCEPTION, message, cause) {
    override fun messageArguments(): Collection<String> = setOf(statusCode.value().toString())
}
