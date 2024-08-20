/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.domain.user.repository

import com.github.francescojo.core.domain.user.UserId
import com.github.francescojo.core.domain.user.exception.UserByIdNotFoundException
import com.github.francescojo.core.domain.user.model.User
import com.github.francescojo.core.repository.WritableRepository
import com.github.francescojo.lib.collection.assertSingleOrNull

/**
 * @since 2021-08-10
 */
interface UserRepository : WritableRepository<User, UserId> {
    override fun getById(id: UserId): User =
        findAllByIds(setOf(id)).assertSingleOrNull() ?: throw UserByIdNotFoundException(id)

    fun findByNickname(nickname: String): User?

    fun findByEmail(email: String): User?

    companion object {
        const val NAME = "com.github.francescojo.core.domain.user.UserRepository"
    }
}
