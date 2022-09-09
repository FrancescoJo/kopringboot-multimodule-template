/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.jdbc.user.repository

import com.github.francescojo.core.domain.user.User
import com.github.francescojo.core.domain.user.repository.UserRepository
import com.github.francescojo.core.jdbc.user.UserEntity
import com.github.francescojo.core.jdbc.user.UserObjectFactoryImpl.toEntity
import com.github.francescojo.core.jdbc.user.dao.UserEntityDao
import com.github.francescojo.lib.annotation.VisibleForTesting
import com.github.francescojo.lib.util.FastCollectedLruCache
import org.springframework.stereotype.Service
import java.util.*

/**
 * @since 2021-08-10
 */
@Service
internal class UserRepositoryImpl(
    private val usersDao: UserEntityDao
) : UserRepository {
    @VisibleForTesting
    val idToUserCache = FastCollectedLruCache.create<UUID, UserEntity>(CACHE_CAPACITY)
    @VisibleForTesting
    val nicknameToUserCache = FastCollectedLruCache.create<String, UserEntity>(CACHE_CAPACITY)
    @VisibleForTesting
    val emailToUserCache = FastCollectedLruCache.create<String, UserEntity>(CACHE_CAPACITY)

    override fun findByUuid(uuid: UUID): UserEntity? =
        (idToUserCache.get(uuid) ?: usersDao.selectByUuid(uuid))?.also { updateCache(it) }

    override fun findByNickname(nickname: String): UserEntity? =
        (nicknameToUserCache.get(nickname) ?: usersDao.selectByNickname(nickname))?.also { updateCache(it) }

    override fun findByEmail(email: String): UserEntity? =
        (emailToUserCache.get(email) ?: usersDao.selectByEmail(email))?.also { updateCache(it) }

    override fun save(user: User): UserEntity {
        val userEntity = (user as? UserEntity) ?: user.toEntity()

        val savedUser = usersDao.upsert(userEntity)

        return savedUser.also { updateCache(it) }
    }

    private fun updateCache(userEntity: UserEntity) {
        idToUserCache.put(userEntity.uuid, userEntity)
        nicknameToUserCache.put(userEntity.nickname, userEntity)
        emailToUserCache.put(userEntity.email, userEntity)
    }

    companion object {
        private const val CACHE_CAPACITY = 100
    }
}
