/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.domain.user.exception

import com.github.francescojo.core.domain.user.UserId
import com.github.francescojo.core.exception.ErrorCodes
import com.github.francescojo.core.exception.external.MalformedInputException

/**
 * @since 2021-08-10
 */
class UserByIdNotFoundException : MalformedInputException {
    private val ids: Collection<UserId>

    constructor(
        id: UserId,
        message: String = errMessage(id),
        cause: Throwable? = null
    ) : this(setOf(id), message, cause)

    constructor(
        ids: Collection<UserId>,
        message: String = errMessage(ids),
        cause: Throwable? = null
    ) : super(ErrorCodes.USER_BY_ID_NOT_FOUND, message, cause) {
        require(ids.isNotEmpty()) { "parameter 'ids' is empty." }

        this.ids = ids
    }

    override fun messageArguments(): Collection<String> = ids.map { it.value.toString() }

    companion object {
        private fun errMessage(id: UserId): String = errMessage(setOf(id))

        private fun errMessage(ids: Collection<UserId>): String = when (ids.size) {
            1 -> "User with id(${ids.single()}) is not found."
            else -> "Users with id: $ids are not found."
        }
    }
}
