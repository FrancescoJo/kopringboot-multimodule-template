/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.domain.user.model

import com.github.francescojo.core.domain.DateAuditable
import com.github.francescojo.core.domain.IdentifiableObject
import com.github.francescojo.core.domain.user.UserId
import com.github.francescojo.core.domain.user.projection.UserProjection
import com.github.francescojo.lib.annotation.ValueParameter
import java.time.Instant

/**
 * A write model that derived from [UserProjection].
 * This model holds general write rules for User.
 *
 * It is advised to retain this model as simple as possible - for example,
 * no business logic, no validation, no default parameters, etc. We assume that all business rules
 * are already done in [UserProjection].
 *
 * @since 2024-08-06
 */
interface User : IdentifiableObject<UserId>, DateAuditable {
    val nickname: String

    val email: String

    companion object {
        const val LENGTH_NAME_MIN = 2
        const val LENGTH_NAME_MAX = 64
        const val LENGTH_EMAIL_MAX = 64

        fun from(@ValueParameter src: UserProjection): User = src.run {
            create(
                id = id,
                nickname = nickname,
                email = email,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }

        fun create(
            @ValueParameter id: UserId,
            @ValueParameter nickname: String,
            @ValueParameter email: String,
            @ValueParameter createdAt: Instant,
            @ValueParameter updatedAt: Instant
        ) : User = UserImpl(
            id = id,
            nickname = nickname,
            email = email,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    private data class UserImpl(
        override val id: UserId,
        override val nickname: String,
        override val email: String,
        override val createdAt: Instant,
        override val updatedAt: Instant
    ) : User
}
