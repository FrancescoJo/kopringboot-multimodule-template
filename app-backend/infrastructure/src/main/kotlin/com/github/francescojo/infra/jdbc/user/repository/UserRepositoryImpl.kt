/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.infra.jdbc.user.repository

import com.github.francescojo.core.domain.user.User
import com.github.francescojo.core.domain.user.repository.writable.UserRepository
import com.github.francescojo.infra.jdbc.user.UserEntity
import org.springframework.stereotype.Service
import java.util.*
import com.github.francescojo.infra.jdbc.user.dao.UserEntityDao as UserEntityJdbcDao

/**
 * @since 2021-08-10
 */
@Service(UserRepository.NAME)
internal class UserRepositoryImpl(
    private val userEntityJdbcDao: UserEntityJdbcDao
) : UserReadonlyRepositoryImpl(
    userEntityJdbcDao = userEntityJdbcDao
), UserRepository {
    override fun save(user: User): User {
        val savedUser = userEntityJdbcDao.selectById(user.id)?.let {
            userEntityJdbcDao.update(it.id, UserEntity.from(user))
        } ?: run {
            userEntityJdbcDao.insert(UserEntity.from(user))
        }

        return updateCache(savedUser)
    }

    override fun deleteById(id: UUID): Boolean =
        userEntityJdbcDao.deleteById(id)
}
