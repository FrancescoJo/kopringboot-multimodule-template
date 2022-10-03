/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.domain.user

import com.github.francescojo.core.domain.SoftDeletable
import com.github.francescojo.core.domain.user.aggregate.UserModel
import com.github.francescojo.core.domain.user.usecase.EditUserUseCase
import java.time.Instant
import java.util.*

/**
 * @since 2021-08-10
 */
interface User : SoftDeletable {
    val id: UUID

    val nickname: String

    val email: String

    val registeredAt: Instant

    val lastActiveAt: Instant

    fun applyValues(values: EditUserUseCase.EditUserMessage): User

    override fun delete(): User

    companion object {
        const val LENGTH_NAME_MIN = 2
        const val LENGTH_NAME_MAX = 64
        const val LENGTH_EMAIL_MAX = 64

        @SuppressWarnings("LongParameterList")      // Intended complexity to provide various User creation cases
        fun create(
            id: UUID = UUID.randomUUID(),
            nickname: String,
            email: String,
            registeredAt: Instant? = null,
            lastActiveAt: Instant? = null,
            deleted: Boolean = false
        ): User {
            val now = Instant.now()

            return UserModel(
                id = id,
                nickname = nickname,
                email = email,
                registeredAt = registeredAt ?: now,
                lastActiveAt = lastActiveAt ?: now,
                deleted = deleted
            )
        }
    }
}
