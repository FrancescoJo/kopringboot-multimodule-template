/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package test.domain.user

import com.github.francescojo.core.domain.user.User
import com.github.francescojo.core.domain.user.UserObjectFactory
import com.github.javafaker.Faker
import java.time.Instant
import java.util.*

/**
 * @since 2021-08-10
 */
object FakeUserObjectFactory : UserObjectFactory {
    override fun user(
        id: UUID,
        nickname: String,
        email: String,
        registeredAt: Instant,
        lastActiveAt: Instant,
        deleted: Boolean
    ): User = FakeUser(
        uuid = id,
        nickname = nickname,
        email = email,
        registeredAt = registeredAt,
        lastActiveAt = lastActiveAt,
        deleted = deleted
    )

    fun randomUser(
        id: UUID = UUID.randomUUID(),
        nickname: String = Faker().name().fullName(),
        email: String = Faker().internet().emailAddress(),
        registeredAt: Instant = Instant.now(),
        lastActiveAt: Instant = Instant.now(),
        deleted: Boolean = false
    ) = user(
        id = id,
        nickname = nickname,
        email = email,
        registeredAt = registeredAt,
        lastActiveAt = lastActiveAt,
        deleted = deleted
    )
}
