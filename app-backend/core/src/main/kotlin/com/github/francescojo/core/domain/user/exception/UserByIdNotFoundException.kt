/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.domain.user.exception

import com.github.francescojo.core.exception.ErrorCodes
import com.github.francescojo.core.exception.external.MalformedInputException
import java.util.*

/**
 * @since 2021-08-10
 */
class UserByIdNotFoundException(
    val id: UUID,
    override val message: String = "User('$id') is not found.",
    override val cause: Throwable? = null
) : MalformedInputException(ErrorCodes.USER_BY_ID_NOT_FOUND, message, cause) {
    override fun messageArguments(): Collection<String> = setOf(id.toString())
}
