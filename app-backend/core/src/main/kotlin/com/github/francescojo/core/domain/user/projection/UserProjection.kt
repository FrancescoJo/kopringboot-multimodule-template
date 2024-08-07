/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.domain.user.projection

import com.github.francescojo.core.domain.DateAuditable
import com.github.francescojo.core.domain.IdentifiableObject
import com.github.francescojo.core.domain.user.UserId
import com.github.francescojo.core.domain.user.model.User
import com.github.francescojo.core.domain.user.projection.impl.UserProjectionImpl
import com.github.francescojo.core.domain.user.usecase.EditUserUseCase
import com.github.francescojo.lib.annotation.ValueParameter
import java.time.Instant

/**
 * A model that holds any business rules for User.
 *
 * This is a read model in CQRS context; must not be used as a write model.
 *
 * @since 2021-08-10
 */
interface UserProjection : IdentifiableObject<UserId>, DateAuditable {
    val nickname: String

    val email: String

    fun mutate(): Mutator = UserProjectionImpl.from(this)

    interface Mutator : UserProjection, DateAuditable.Mutator {
        override var nickname: String

        override var email: String

        fun applyValues(
            values: EditUserUseCase.EditUserMessage,
            updatedAt: Instant = Instant.now()
        ): UserProjection
    }

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

        @SuppressWarnings("LongParameterList")      // Intended complexity to provide various User creation cases
        fun create(
            @ValueParameter id: UserId,
            @ValueParameter nickname: String,
            @ValueParameter email: String,
            @ValueParameter createdAt: Instant = Instant.now(),
            @ValueParameter updatedAt: Instant = createdAt
        ): UserProjection = UserProjectionImpl(
            id = id,
            nickname = nickname,
            email = email,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
