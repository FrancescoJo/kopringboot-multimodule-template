/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.jdbc.user.repository

import com.github.francescojo.core.domain.user.User
import com.github.francescojo.core.domain.user.repository.UserReadonlyRepository
import com.github.francescojo.core.jdbc.user.UserEntity
import com.github.francescojo.lib.annotation.VisibleForTesting
import com.github.francescojo.lib.util.FastCollectedLruCache
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import java.util.*
import com.github.francescojo.core.jdbc.user.dao.UserEntityDao as UserEntityJdbcDao

/**
 * @since 2021-08-10
 */
@Primary
@Service(UserReadonlyRepository.NAME)
internal open class UserReadonlyRepositoryImpl(
    private val userEntityJdbcDao: UserEntityJdbcDao
) : UserReadonlyRepository {
    @VisibleForTesting
    val idToUserCache = FastCollectedLruCache.create<UUID, User>(CACHE_CAPACITY)

    @VisibleForTesting
    val nicknameToUserCache = FastCollectedLruCache.create<String, User>(CACHE_CAPACITY)

    @VisibleForTesting
    val emailToUserCache = FastCollectedLruCache.create<String, User>(CACHE_CAPACITY)

    override fun findByUuid(uuid: UUID): User? =
        idToUserCache.get(uuid) ?: userEntityJdbcDao.selectById(uuid)?.let { updateCache(it) }

    override fun findByNickname(nickname: String): User? =
        (nicknameToUserCache.get(nickname) ?: userEntityJdbcDao.selectByNickname(nickname)?.let { updateCache(it) })

    override fun findByEmail(email: String): User? =
        (emailToUserCache.get(email) ?: userEntityJdbcDao.selectByEmail(email)?.let { updateCache(it) })

    protected fun updateCache(userEntity: UserEntity): User {
        val user = userEntity.toUser()

        idToUserCache.put(user.id, user)
        nicknameToUserCache.put(user.nickname, user)
        emailToUserCache.put(user.email, user)

        return user
    }

    companion object {
        internal const val CACHE_CAPACITY = 100
    }
}
