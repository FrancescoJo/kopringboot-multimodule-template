/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.infra.jdbc.user.dao

import com.github.francescojo.infra.jdbc.AbstractJdbcTemplateDao
import com.github.francescojo.infra.jdbc.user.UserEntity
import io.hypersistence.tsid.TSID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

/**
 * @since 2021-08-10
 */
// TODO: Require transaction
internal interface UserEntityDao {
    fun selectAllByIds(ids: Collection<TSID>, lockRecords: Boolean): List<UserEntity>

    fun selectAllByNicknames(nicknames: Collection<String>): List<UserEntity>

    fun selectAllByEmails(emails: Collection<String>): List<UserEntity>

    fun insertAll(entities: Collection<UserEntity>): List<UserEntity>

    fun updateAll(entities: Collection<UserEntity>): List<UserEntity>

    fun deleteAllByIds(ids: Collection<TSID>): Long
}

@Repository
internal interface UserEntityJpaRepository : JpaRepository<UserEntity, Long> {
    @Query("""
        SELECT u
        FROM UserEntity u
        WHERE u.id IN ?1
          AND u.deleted.isDeleted = FALSE
    """)
    fun findAllById(ids: Collection<Long>): List<UserEntity>

    @Query("""
        SELECT u
        FROM UserEntity u
        WHERE u.nickname IN ?1
          AND u.deleted.isDeleted = FALSE
    """)
    fun findAllByNicknames(nicknames: Collection<String>): List<UserEntity>

    @Query("""
        SELECT u
        FROM UserEntity u
        WHERE u.email IN ?1
          AND u.deleted.isDeleted = FALSE
    """)
    fun findAllByEmails(emails: Collection<String>): List<UserEntity>
}

@Repository
internal class UserEntityDaoImpl(
    private val userEntityJpaDao: UserEntityJpaRepository,

    jdbcTemplate: JdbcTemplate
) : AbstractJdbcTemplateDao(jdbcTemplate), UserEntityDao {
    override fun selectAllByIds(ids: Collection<TSID>, lockRecords: Boolean): List<UserEntity> =
        userEntityJpaDao.findAllById(ids.map { it.toLong() })

    override fun selectAllByNicknames(nicknames: Collection<String>): List<UserEntity> =
        userEntityJpaDao.findAllByNicknames(nicknames)

    override fun selectAllByEmails(emails: Collection<String>): List<UserEntity> =
        userEntityJpaDao.findAllByEmails(emails)

    override fun insertAll(entities: Collection<UserEntity>): List<UserEntity> =
        userEntityJpaDao.saveAll(entities)

    override fun updateAll(entities: Collection<UserEntity>): List<UserEntity> =
        userEntityJpaDao.saveAll(entities)

    override fun deleteAllByIds(ids: Collection<TSID>): Long =
        userEntityJpaDao.findAllById(ids.map { it.toLong() }).run {
            onEach { it.deleted.isDeleted = true }
            userEntityJpaDao.saveAll(this)
        }.size.toLong()
}
