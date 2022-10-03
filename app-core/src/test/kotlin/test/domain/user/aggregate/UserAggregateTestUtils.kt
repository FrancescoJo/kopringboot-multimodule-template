/*
 * kopringboot-multimodule-template
 * Sir.LOIN Intellectual property. All rights reserved.
 */
package test.domain.user.aggregate

import com.github.francescojo.core.domain.user.User
import com.github.javafaker.Faker
import java.time.Instant
import java.util.*

fun randomUser(
    id: UUID = UUID.randomUUID(),
    nickname: String = Faker().name().fullName(),
    email: String = Faker().internet().emailAddress(),
    registeredAt: Instant = Instant.now(),
    lastActiveAt: Instant = Instant.now(),
    deleted: Boolean = false
) = User.create(
    id = id,
    nickname = nickname,
    email = email,
    registeredAt = registeredAt,
    lastActiveAt = lastActiveAt,
    deleted = deleted
)
