/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package test.domain.user

import com.github.francescojo.core.domain.user.User
import java.time.Instant
import java.util.*

/**
 * @since 2021-08-10
 */
data class FakeUser(
    override val uuid: UUID,
    override var nickname: String,
    override var email: String,
    override var registeredAt: Instant,
    override var lastActiveAt: Instant,
    override var deleted: Boolean
) : User
