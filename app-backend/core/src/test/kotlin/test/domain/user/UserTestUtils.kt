/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package test.domain.user

import com.github.francescojo.core.domain.TsidValueHolder
import com.github.francescojo.core.domain.user.UserId
import com.github.francescojo.core.domain.user.projection.UserProjection
import io.hypersistence.tsid.TSID
import test.com.github.francescojo.lib.SharedTestObjects.faker
import java.time.Instant

/**
 * @since 2021-08-10
 */
object UserTestUtils {
    @JvmStatic
    fun randomUserId(): UserId = UserId(TSID.fast())

    @JvmStatic
    fun emptyUserId(): UserId = UserId(TsidValueHolder.EMPTY_VALUE)

    fun randomUserProjection(
        id: UserId = UserId(TSID.fast()),
        nickname: String = faker.name().fullName(),
        email: String = faker.internet().emailAddress(),
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
