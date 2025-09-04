/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.exception.external

import com.github.francescojo.core.exception.ErrorCodes
import com.github.francescojo.core.exception.ExternalException

/**
 * @since 2021-08-10
 */
class WrongPresentationRequestException(
    private val format: String = "",
    override val message: String = if (format.isBlank()) {
        ErrorCodes.WRONG_PRESENTATION.defaultMessage
    } else {
        "Data presentation is not supported: '$format'"
    },
    override val cause: Throwable? = null
) : ExternalException(ErrorCodes.WRONG_PRESENTATION, message, cause)
