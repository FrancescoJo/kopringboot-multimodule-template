/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.exception.external

import com.github.francescojo.core.exception.ErrorCodes
import com.github.francescojo.core.exception.ExternalException

/**
 * Indicates that an external input is unrecognisable.
 *
 * @since 2021-08-10
 */
open class MalformedInputException(
    override val errorCode: ErrorCodes = ErrorCodes.MALFORMED_INPUT,
    override val message: String = ErrorCodes.MALFORMED_INPUT.defaultMessage,
    override val cause: Throwable? = null
) : ExternalException(errorCode, message, cause)
