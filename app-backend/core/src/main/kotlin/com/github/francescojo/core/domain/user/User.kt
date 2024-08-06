/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.domain.user

import com.github.francescojo.core.domain.user.usecase.EditUserUseCase
import com.github.francescojo.core.domain.user.vo.UserModel
import java.time.Instant
import java.util.*

/**
 * @since 2021-08-10
 */
interface User {
    val id: UUID

    val nickname: String

    val email: String

    val registeredAt: Instant

    val lastActiveAt: Instant

    fun mutate(): Mutator = UserModel.from(this)

    interface Mutator : User {
        override var nickname: String

        override var email: String

        override var lastActiveAt: Instant

        fun applyValues(
            values: EditUserUseCase.EditUserMessage,
            modifiedAt: Instant = Instant.now()
        ): User
    }

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
            lastActiveAt: Instant? = null
        ): User = UserModel.create(
            id = id,
            nickname = nickname,
            email = email,
            registeredAt = registeredAt,
            lastActiveAt = lastActiveAt
        )
    }
}
