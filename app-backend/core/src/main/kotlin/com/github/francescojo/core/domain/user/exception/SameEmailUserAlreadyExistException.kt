/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.domain.user.exception

import com.github.francescojo.core.exception.ErrorCodes
import com.github.francescojo.core.exception.ExternalException

/**
 * @since 2021-08-10
 */
class SameEmailUserAlreadyExistException(
    private val email: String,
    override val message: String = if (email.isBlank()) {
        ErrorCodes.USER_BY_EMAIL_DUPLICATED.defaultMessage
    } else {
        "User exists with email: '$email'"
    },
    override val cause: Throwable? = null
) : ExternalException(ErrorCodes.USER_BY_EMAIL_DUPLICATED, message, cause) {
    override fun messageArguments(): Collection<String> = setOf(email)
}
