/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package test.domain.user

import com.github.francescojo.core.domain.TsidValueHolder
import com.github.francescojo.core.domain.user.UserId
import com.github.francescojo.core.domain.user.projection.UserProjection
import com.github.javafaker.Faker
import io.hypersistence.tsid.TSID
import java.time.Instant

/**
 * @since 2021-08-10
 */
object UserTestUtils {
    fun UserId.Companion.random(): UserId = UserId(TSID.fast())

    val UserId.Companion.EMPTY: UserId
        get() = UserId(TsidValueHolder.EMPTY_VALUE)

    fun randomUserProjection(
        id: UserId = UserId(TSID.fast()),
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
}
