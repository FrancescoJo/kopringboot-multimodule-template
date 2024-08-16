/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.domain.user.projection

import com.github.francescojo.core.domain.DateAuditable
import com.github.francescojo.core.domain.IdentifiableObject
import com.github.francescojo.core.domain.user.UserId
import com.github.francescojo.core.domain.user.model.User
import com.github.francescojo.lib.annotation.ValueParameter
import java.time.Instant

/**
 * A 'read model' in CQRS context that holds any necessary information of User business rules.
 *
 * @since 2021-08-10
 */
interface UserProjection : IdentifiableObject<UserId>, DateAuditable {
    val nickname: String

    val email: String

    companion object {
        fun aggregate(
            @ValueParameter user: User
        ) : UserProjection {
            return create(
                id = user.id,
                nickname = user.nickname,
                email = user.email,
                createdAt = user.createdAt,
                updatedAt = user.updatedAt
            )
        }

        fun create(
            @ValueParameter id: UserId,
            @ValueParameter nickname: String,
            @ValueParameter email: String,
            @ValueParameter createdAt: Instant,
            @ValueParameter updatedAt: Instant
        ): UserProjection = UserProjectionImpl(
            id = id,
            nickname = nickname,
            email = email,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    private data class UserProjectionImpl(
        override val id: UserId,
        override val nickname: String,
        override val email: String,
        override val createdAt: Instant,
        override val updatedAt: Instant
    ) : UserProjection
}
