/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.domain.user

import com.github.francescojo.core.domain.SoftDeletable
import com.github.francescojo.core.domain.user.usecase.EditUserUseCase
import java.time.Instant
import java.util.*

/**
 * @since 2021-08-10
 */
interface User : SoftDeletable {
    val uuid: UUID

    var nickname: String

    var email: String

    var registeredAt: Instant

    var lastActiveAt: Instant

    fun applyValues(values: EditUserUseCase.EditUserMessage): User {
        this.nickname = values.nickname ?: this.nickname
        this.email = values.email ?: this.email
        this.lastActiveAt = Instant.now()
        return this
    }

    fun delete(): User {
        this.deleted = true
        this.lastActiveAt = Instant.now()
        return this
    }

    companion object {
        const val LENGTH_NAME_MIN = 2
        const val LENGTH_NAME_MAX = 64
        const val LENGTH_EMAIL_MAX = 64
    }
}
