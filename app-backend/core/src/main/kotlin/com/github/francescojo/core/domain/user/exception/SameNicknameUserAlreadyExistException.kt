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
class SameNicknameUserAlreadyExistException(
    private val nickname: String,
    override val message: String = if (nickname.isBlank()) {
        ErrorCodes.USER_BY_NICKNAME_DUPLICATED.defaultMessage
    } else {
        "User exists with nickname: '$nickname'"
    },
    override val cause: Throwable? = null
) : ExternalException(ErrorCodes.USER_BY_NICKNAME_DUPLICATED, message, cause) {
    override fun messageArguments(): Collection<String> = setOf(nickname)
}
