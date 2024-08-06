/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.exception.internal

import com.github.francescojo.core.exception.ErrorCodes
import com.github.francescojo.core.exception.InternalException

/**
 * @since 2024-08-06
 */
class DataIntegrityBrokenException(
    override val message: String = "Data integrity is broken.",
    override val cause: Throwable? = null
) : InternalException(ErrorCodes.DATA_INTEGRITY_BROKEN, message, cause)
