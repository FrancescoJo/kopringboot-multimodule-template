/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.domain.user.model

import com.github.francescojo.core.domain.DateAuditable
import com.github.francescojo.core.domain.IdentifiableObject
import com.github.francescojo.core.domain.user.UserId
import com.github.francescojo.core.domain.user.model.impl.UserMutator
import com.github.francescojo.core.domain.user.projection.UserProjection
import com.github.francescojo.lib.annotation.ValueParameter
import io.hypersistence.tsid.TSID
import java.time.Instant

/**
 * A 'write model' that derived from [UserProjection].
 * This model holds general write rules for User.
 *
 * @since 2024-08-06
 */
interface User : IdentifiableObject<UserId>, DateAuditable {
    val nickname: String

    val email: String

    fun mutate(): Mutator = UserMutator.from(this)

    interface Mutator : User, DateAuditable.Mutator {
        override var nickname: String

        override var email: String
    }

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
            @ValueParameter id: UserId = UserId(TSID.Factory.getTsid()),
            @ValueParameter nickname: String,
            @ValueParameter email: String,
            @ValueParameter createdAt: Instant = Instant.now(),
            @ValueParameter updatedAt: Instant = createdAt
        ) : User = UserMutator(
            id = id,
            nickname = nickname,
            email = email,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
