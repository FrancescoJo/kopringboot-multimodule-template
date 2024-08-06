/*
 * kopringboot-multimodule-template
 * Distributed under CC BY-NC-SA
 */
package com.github.francescojo.core.domain.user.repository.writable

import com.github.francescojo.core.domain.user.User
import com.github.francescojo.core.domain.user.repository.UserReadonlyRepository

/**
 * @since 2021-08-10
 */
interface UserRepository : UserReadonlyRepository {
    fun save(user: User): User

    companion object {
        const val NAME = "com.github.francescojo.core.domain.user.UserRepository"
    }
}
