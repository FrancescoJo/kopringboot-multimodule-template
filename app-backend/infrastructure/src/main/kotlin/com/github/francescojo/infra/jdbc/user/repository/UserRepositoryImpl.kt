/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.infra.jdbc.user.repository

import com.github.francescojo.core.domain.user.UserId
import com.github.francescojo.core.domain.user.model.User
import com.github.francescojo.core.domain.user.repository.UserRepository
import com.github.francescojo.infra.jdbc.user.UserEntity
import com.github.francescojo.infra.jdbc.user.dao.UserEntityDao
import com.github.francescojo.infra.repository.AbstractWritableRepositoryTemplate
import com.github.francescojo.lib.util.assertSingleOrNull
import org.springframework.stereotype.Service

/**
 * @since 2021-08-10
 */
// TODO: Require transaction
@Service(UserRepository.NAME)
internal class UserRepositoryImpl(
    private val userEntityDao: UserEntityDao
) : AbstractWritableRepositoryTemplate<User, UserId>(), UserRepository, UserRepositorySupport {
    override fun createAll(models: Collection<User>): List<User> {
        return aggregateAll { userEntityDao.insertAll(models.map { it.toUserEntity() }) }
    }

    override fun findAllByIds(ids: Collection<UserId>, lockRecords: Boolean): List<User> {
        return aggregateAll { userEntityDao.selectAllByIds(ids.map { it.value }, lockRecords) }
    }

    override fun updateAll(models: Collection<User>): List<User> {
        return aggregateAll { userEntityDao.updateAll(models.map { it.toUserEntity() }) }
    }

    override fun deleteAllByIds(ids: Collection<UserId>): Long =
        userEntityDao.deleteAllByIds(ids.map { it.value })

    override fun findByNickname(nickname: String): User? {
        return aggregateAll { userEntityDao.selectAllByNicknames(setOf(nickname)) }.assertSingleOrNull()
    }

    override fun findByEmail(email: String): User? {
        return aggregateAll { userEntityDao.selectAllByEmails(setOf(email)) }.assertSingleOrNull()
    }

    private fun aggregateAll(
        operation: () -> List<UserEntity>
    ): List<User> {
        return operation().map { it.toUser() }
    }
}
