/*
 * kopringboot-multimodule-template
 * Distributed under CC BY-NC-SA
 */
package com.github.francescojo.core.domain.user.vo

import com.github.francescojo.core.domain.user.User
import com.github.francescojo.core.domain.user.usecase.EditUserUseCase
import java.time.Instant
import java.util.*

/**
 * Implementation note is taken from: https://martinfowler.com/bliki/EvansClassification.html
 *
 * @since 2022-10-03
 */
internal data class UserModel(
    override val id: UUID,
    override var nickname: String,
    override var email: String,
    override val registeredAt: Instant,
    override var lastActiveAt: Instant
) : User.Mutator {
    override fun applyValues(
        values: EditUserUseCase.EditUserMessage,
        modifiedAt: Instant
    ): User = this.apply {
        nickname = values.nickname ?: this.nickname
        email = values.email ?: this.email
        lastActiveAt = modifiedAt
    }

    companion object {
        fun from(src: User) = src.run {
            create(
                id = id,
                nickname = nickname,
                email = email,
                registeredAt = registeredAt,
                lastActiveAt = lastActiveAt
            )
        }

        @SuppressWarnings("LongParameterList")      // Intended complexity to provide various User creation cases
        fun create(
            id: UUID = UUID.randomUUID(),
            nickname: String,
            email: String,
            registeredAt: Instant? = null,
            lastActiveAt: Instant? = null
        ): UserModel {
            val now = Instant.now()

            return UserModel(
                id = id,
                nickname = nickname,
                email = email,
                registeredAt = registeredAt ?: now,
                lastActiveAt = lastActiveAt ?: now
            )
        }
    }
}
