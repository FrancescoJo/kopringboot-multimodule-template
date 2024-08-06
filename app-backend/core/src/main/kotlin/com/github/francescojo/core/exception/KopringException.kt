/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.exception

/**
 * @since 2021-08-10
 */
sealed class KopringException(
    open val codeBook: ErrorCodes,
    override val message: String = "",
    override val cause: Throwable? = null,
) : RuntimeException(message, cause) {
    open fun messageArguments(): Collection<String>? = null

    open fun details(): Any? = null
}

/**
 * @since 2022-02-14
 */
open class ExternalException constructor(
    codeBook: ErrorCodes,
    override val message: String = "",
    override val cause: Throwable? = null,
) : KopringException(codeBook, message, cause)

/**
 * @since 2022-02-14
 */
open class InternalException constructor(
    codeBook: ErrorCodes,
    override val message: String = "",
    override val cause: Throwable? = null,
) : KopringException(codeBook, message, cause)
