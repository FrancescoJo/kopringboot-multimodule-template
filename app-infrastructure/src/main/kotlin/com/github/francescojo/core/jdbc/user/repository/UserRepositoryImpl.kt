/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.jdbc.user.repository

import com.github.francescojo.core.domain.user.User
import com.github.francescojo.core.domain.user.repository.UserRepository
import com.github.francescojo.core.jdbc.user.UserEntity
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
    val idToUserCache = FastCollectedLruCache.create<UUID, User>(CACHE_CAPACITY)

    @VisibleForTesting
    val nicknameToUserCache = FastCollectedLruCache.create<String, User>(CACHE_CAPACITY)

    @VisibleForTesting
    val emailToUserCache = FastCollectedLruCache.create<String, User>(CACHE_CAPACITY)

    override fun findByUuid(uuid: UUID): User? =
        (idToUserCache.get(uuid) ?: usersDao.selectByUuid(uuid)?.let { updateCache(it) })

    override fun findByNickname(nickname: String): User? =
        (nicknameToUserCache.get(nickname) ?: usersDao.selectByNickname(nickname)?.let { updateCache(it) })

    override fun findByEmail(email: String): User? =
        (emailToUserCache.get(email) ?: usersDao.selectByEmail(email)?.let { updateCache(it) })

    override fun save(user: User): User {
        val savedUser = usersDao.selectByUuid(user.id)?.let {
            usersDao.update(it.seq!!, UserEntity.from(user))
        } ?: UserEntity.from(user)

        return updateCache(savedUser)
    }

    private fun updateCache(userEntity: UserEntity): User {
        val user = userEntity.toUser()

        idToUserCache.put(user.id, user)
        nicknameToUserCache.put(user.nickname, user)
        emailToUserCache.put(user.email, user)

        return user
    }

    companion object {
        private const val CACHE_CAPACITY = 100
    }
}
