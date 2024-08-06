/*
 * kopringboot-multimodule-template
 * Distributed under CC BY-NC-SA
 */
package com.github.francescojo.core.domain.user.repository.writable

import com.github.francescojo.core.domain.user.User
import com.github.francescojo.core.domain.user.repository.UserReadonlyRepository
import java.util.*

/**
 * @since 2021-08-10
 */
interface UserRepository : UserReadonlyRepository {
    fun save(user: User): User

    fun deleteById(id: UUID): Boolean

    companion object {
        const val NAME = "com.github.francescojo.core.domain.user.UserRepository"
    }
}
