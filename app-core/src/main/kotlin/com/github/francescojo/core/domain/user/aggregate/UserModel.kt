/*
 * kopringboot-multimodule-template
 * Sir.LOIN Intellectual property. All rights reserved.
 */
package com.github.francescojo.core.domain.user.aggregate

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
    override val nickname: String,
    override val email: String,
    override val registeredAt: Instant,
    override val lastActiveAt: Instant,
    override val deleted: Boolean
) : User {
    override fun applyValues(values: EditUserUseCase.EditUserMessage): User = this.copy(
        nickname = values.nickname ?: this.nickname,
        email = values.email ?: this.email,
        lastActiveAt = Instant.now()
    )

    override fun delete(): User = this.copy(
        deleted = true,
        lastActiveAt = Instant.now()
    )
}
