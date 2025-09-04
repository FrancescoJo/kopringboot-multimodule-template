/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.exception.external

import com.github.francescojo.core.exception.ErrorCodes
import com.github.francescojo.core.exception.ExternalException

/**
 * No resource is found to handle external request.
 *
 * @since 2021-08-10
 */
class RequestedResourceNotFoundException(
    private val name: String,
    override val message: String = if (name.isBlank()) {
        ErrorCodes.RESOURCE_NOT_FOUND.defaultMessage
    } else {
        "Requested resource is not found: '$name'."
    },
    override val cause: Throwable? = null
) : ExternalException(ErrorCodes.RESOURCE_NOT_FOUND, message, cause) {
    override fun messageArguments(): Collection<String>? = setOf(name)
}
