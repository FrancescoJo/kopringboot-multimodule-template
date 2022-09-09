/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.domain.user

import java.time.Instant
import java.util.*

/**
 * @since 2021-08-10
 */
interface UserObjectFactory {
    @SuppressWarnings("LongParameterList")      // Intended complexity
    fun user(
        id: UUID,
        nickname: String,
        email: String,
        registeredAt: Instant,
        lastActiveAt: Instant,
        deleted: Boolean
    ): User

    fun user(
        id: UUID = UUID.randomUUID(),
        nickname: String,
        email: String,
        deleted: Boolean = false
    ): User {
        val now = Instant.now()

        return user(
            id = id,
            nickname = nickname,
            email = email,
            registeredAt = now,
            lastActiveAt = now,
            deleted = deleted
        )
    }
}
