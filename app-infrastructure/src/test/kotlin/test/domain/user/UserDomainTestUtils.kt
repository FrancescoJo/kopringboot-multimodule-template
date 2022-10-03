/*
 * kopringboot-multimodule-template
 * Sir.LOIN Intellectual property. All rights reserved.
 */
package test.domain.user

import com.github.francescojo.core.jdbc.user.UserEntity
import com.github.javafaker.Faker
import test.domain.user.aggregate.randomUser
import java.time.Instant
import java.util.*

internal fun randomUserEntity(
    id: UUID = UUID.randomUUID(),
    nickname: String = Faker().name().fullName(),
    email: String = Faker().internet().emailAddress(),
    registeredAt: Instant = Instant.now(),
    lastActiveAt: Instant = Instant.now(),
    deleted: Boolean = false
): UserEntity = UserEntity.from(
    randomUser(
        id = id,
        nickname = nickname,
        email = email,
        registeredAt = registeredAt,
        lastActiveAt = lastActiveAt,
        deleted = deleted
    )
)
