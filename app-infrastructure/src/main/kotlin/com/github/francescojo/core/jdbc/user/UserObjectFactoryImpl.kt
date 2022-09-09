/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.jdbc.user

import com.github.francescojo.core.domain.user.User
import com.github.francescojo.core.domain.user.UserObjectFactory
import java.time.Instant
import java.util.*

/**
 * @since 2021-08-10
 */
internal object UserObjectFactoryImpl : UserObjectFactory {
    override fun user(
        id: UUID,
        nickname: String,
        email: String,
        registeredAt: Instant,
        lastActiveAt: Instant,
        deleted: Boolean
    ) = UserEntity(
        uuid = id,
        nickname = nickname,
        email = email,
        registeredAt = registeredAt,
        lastActiveAt = lastActiveAt,
        deleted = deleted
    )

    fun User.toEntity() = user(
        id = uuid,
        nickname = nickname,
        email = email,
        registeredAt = registeredAt,
        lastActiveAt = lastActiveAt,
        deleted = deleted
    )
}
