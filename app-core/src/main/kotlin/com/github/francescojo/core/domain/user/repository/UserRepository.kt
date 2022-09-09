/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.domain.user.repository

import com.github.francescojo.core.domain.user.User
import java.util.*

/**
 * @since 2021-08-10
 */
interface UserRepository {
    fun findByUuid(uuid: UUID): User?

    fun findByNickname(nickname: String): User?

    fun findByEmail(email: String): User?

    fun save(user: User): User
}
