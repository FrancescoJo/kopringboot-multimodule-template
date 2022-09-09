/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.exception.internal

import com.github.francescojo.core.exception.ErrorCodes
import com.github.francescojo.core.exception.InternalException

/**
 * @since 2021-08-10
 */
class UnhandledException(
    override val message: String = "Unhandled internal error. Sorry for your inconvenience.",
    override val cause: Throwable? = null
) : InternalException(ErrorCodes.UNHANDLED_EXCEPTION, message, cause)
