/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.exception.external

import com.github.francescojo.core.exception.ErrorCodes

/**
 * @since 2021-08-10
 */
class WrongInputException(
    private val value: Any,
    override val message: String = "'$value' is not a valid input value.",
    override val cause: Throwable? = null
) : MalformedInputException(ErrorCodes.WRONG_INPUT, message, cause) {
    override fun messageArguments(): Collection<String> = setOf(value.toString())
}
