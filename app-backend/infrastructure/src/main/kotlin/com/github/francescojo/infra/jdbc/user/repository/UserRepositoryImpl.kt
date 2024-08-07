/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.infra.jdbc.user.repository

import com.github.francescojo.core.domain.user.UserId
import com.github.francescojo.core.domain.user.model.User
import com.github.francescojo.core.domain.user.repository.UserRepository
import com.github.francescojo.infra.jdbc.user.dao.UserEntityDao
import com.github.francescojo.infra.repository.AbstractWritableRepositoryTemplate
import org.springframework.stereotype.Service

/**
 * @since 2021-08-10
 */
@Service(UserRepository.NAME)
internal class UserRepositoryImpl(
    private val userEntityDao: UserEntityDao
) : AbstractWritableRepositoryTemplate<User, UserId>(), UserRepository {
    override fun createAll(models: Collection<User>): List<User> {
        TODO("Not yet implemented")
    }

    override fun findAllByIds(ids: Collection<UserId>, lockRecords: Boolean): List<User> {
        TODO("Not yet implemented")
    }

    override fun updateAll(models: Collection<User>): List<User> {
        TODO("Not yet implemented")
    }

    override fun deleteAllByIds(ids: Collection<UserId>): Long {
        TODO("Not yet implemented")
    }

    override fun findByNickname(nickname: String): User? {
        TODO("Not yet implemented")
    }

    override fun findByEmail(email: String): User? {
        TODO("Not yet implemented")
    }
}
