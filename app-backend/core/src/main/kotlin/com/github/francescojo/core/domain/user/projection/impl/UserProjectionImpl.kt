/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.domain.user.projection.impl

import com.github.francescojo.core.domain.user.UserId
import com.github.francescojo.core.domain.user.projection.UserProjection
import com.github.francescojo.core.domain.user.usecase.EditUserUseCase
import com.github.francescojo.lib.annotation.ValueParameter
import java.time.Instant

/**
 * @since 2022-10-03
 */
internal data class UserProjectionImpl(
    override val id: UserId,
    override var nickname: String,
    override var email: String,
    override var createdAt: Instant,
    override var updatedAt: Instant
) : UserProjection.Mutator {
    override fun applyValues(
        values: EditUserUseCase.EditUserMessage,
        updatedAt: Instant
    ): UserProjection = this.apply {
        this.nickname = values.nickname ?: this.nickname
        this.email = values.email ?: this.email
        this.updatedAt = updatedAt
    }

    companion object {
        fun from(@ValueParameter src: UserProjection) : UserProjectionImpl = src.run {
            UserProjectionImpl(
                id = id,
                nickname = nickname,
                email = email,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }
    }
}
