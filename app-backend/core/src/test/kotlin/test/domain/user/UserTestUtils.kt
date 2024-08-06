/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package test.domain.user

import com.github.francescojo.core.domain.UuidValueHolder
import com.github.francescojo.core.domain.user.UserId
import com.github.francescojo.core.domain.user.projection.UserProjection
import com.github.javafaker.Faker
import java.time.Instant
import java.util.*

fun UserId.Companion.random(): UserId = UserId(UUID.randomUUID())

val UserId.Companion.EMPTY: UserId
    get() = UserId(UuidValueHolder.EMPTY_VALUE)

fun randomUserProjection(
    id: UserId = UserId(UUID.randomUUID()),
    nickname: String = Faker().name().fullName(),
    email: String = Faker().internet().emailAddress(),
    createdAt: Instant = Instant.now(),
    updatedAt: Instant = Instant.now()
) = UserProjection.create(
    id = id,
    nickname = nickname,
    email = email,
    createdAt = createdAt,
    updatedAt = updatedAt
)
