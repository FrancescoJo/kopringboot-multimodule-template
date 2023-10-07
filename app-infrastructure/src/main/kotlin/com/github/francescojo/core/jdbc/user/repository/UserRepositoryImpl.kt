/*
 * kopringboot-multimodule-template
 * Distributed under CC BY-NC-SA
 */
package com.github.francescojo.core.jdbc.user.repository

import com.github.francescojo.core.domain.user.User
import com.github.francescojo.core.domain.user.repository.writable.UserRepository
import com.github.francescojo.core.jdbc.user.UserEntity
import org.springframework.stereotype.Service
import com.github.francescojo.core.jdbc.user.dao.UserEntityDao as UserEntityJdbcDao

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
}
