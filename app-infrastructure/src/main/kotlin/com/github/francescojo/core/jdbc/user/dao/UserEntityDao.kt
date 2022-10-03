/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.jdbc.user.dao

import com.github.francescojo.core.jdbc.JdbcTemplateHelper
import com.github.francescojo.core.jdbc.user.UserEntity
import com.github.francescojo.lib.util.toByteArray
import org.springframework.dao.IncorrectResultSizeDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*

/**
 * @since 2021-08-10
 */
internal interface UserEntityDao {
    fun selectById(id: Long): UserEntity?

    fun selectByUuid(uuid: UUID): UserEntity?

    fun selectByNickname(nickname: String): UserEntity?

    fun selectByEmail(email: String): UserEntity?

    fun insert(userEntity: UserEntity): UserEntity

    fun update(seq: Long, userEntity: UserEntity): UserEntity
}

@Repository
internal class UserEntityDaoImpl(
    override val jdbcTemplate: JdbcTemplate
) : JdbcTemplateHelper(), UserEntityDao {
    override fun selectById(id: Long): UserEntity? {
        val sql = """
            SELECT *
            FROM `${UserEntity.TABLE}` u
            WHERE u.`${UserEntity.COL_SEQ}` = ?
              AND u.`${UserEntity.COL_DELETED}` = FALSE
        """.trimIndent()

        return selectOne(sql, id)
    }

    override fun selectByUuid(uuid: UUID): UserEntity? {
        val sql = """
            SELECT *
            FROM `${UserEntity.TABLE}` u
            WHERE u.`${UserEntity.COL_UUID}` = ?
              AND u.`${UserEntity.COL_DELETED}` = FALSE
        """.trimIndent()

        return selectOne(sql, uuid.toByteArray())
    }

    override fun selectByNickname(nickname: String): UserEntity? {
        val sql = """
            SELECT *
            FROM `${UserEntity.TABLE}` u
            WHERE u.`${UserEntity.COL_NICKNAME}` = ?
              AND u.`${UserEntity.COL_DELETED}` = FALSE
        """.trimIndent()

        return selectOne(sql, nickname)
    }

    override fun selectByEmail(email: String): UserEntity? {
        val sql = """
            SELECT *
            FROM `${UserEntity.TABLE}` u
            WHERE u.`${UserEntity.COL_EMAIL}` = ?
              AND u.`${UserEntity.COL_DELETED}` = FALSE
        """.trimIndent()

        return selectOne(sql, email)
    }

    override fun insert(userEntity: UserEntity): UserEntity {
        val sql = """
            INSERT INTO `${UserEntity.TABLE}` (
                `${UserEntity.COL_UUID}`,
                `${UserEntity.COL_NICKNAME}`,
                `${UserEntity.COL_EMAIL}`,
                `${UserEntity.COL_DELETED}`,
                `${UserEntity.COL_CREATED_AT}`,
                `${UserEntity.COL_UPDATED_AT}`,
                `${UserEntity.COL_VERSION}`
            )
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """.trimIndent()

        return userEntity.apply {
            @Suppress("MagicNumber")    // Not a magic number in this context
            seq = super.doInsertAndGetId(UserEntity.COL_SEQ, sql) {
                setBinaryEx(1, userEntity.uuid.toByteArray())
                setStringEx(2, userEntity.nickname)
                setStringEx(3, userEntity.email)
                setBooleanEx(4, userEntity.deleted)
                setTimestampEx(5, userEntity.registeredAt)
                setTimestampEx(6, userEntity.lastActiveAt)
                setLongEx(7, userEntity.version)
            }.key!!.toLong()
        }
    }

    override fun update(seq: Long, userEntity: UserEntity): UserEntity {
        val sql = """
            UPDATE `${UserEntity.TABLE}`
            SET `${UserEntity.COL_UUID}` = ?,
                `${UserEntity.COL_NICKNAME}` = ?,
                `${UserEntity.COL_EMAIL}` = ?,
                `${UserEntity.COL_DELETED}` = ?,
                `${UserEntity.COL_CREATED_AT}` = ?,
                `${UserEntity.COL_UPDATED_AT}` = ?,
                `${UserEntity.COL_VERSION}` = ?
            WHERE `${UserEntity.COL_SEQ}` = ?
        """.trimIndent()

        @Suppress("MagicNumber")    // Not a magic number in this context
        val affectedRows = super.doUpdate(UserEntity.COL_SEQ, sql) {
            setBinaryEx(1, userEntity.uuid.toByteArray())
            setStringEx(2, userEntity.nickname)
            setStringEx(3, userEntity.email)
            setBooleanEx(4, userEntity.deleted)
            setTimestampEx(5, userEntity.registeredAt)
            setTimestampEx(6, userEntity.lastActiveAt)
            setLongEx(7, userEntity.version)
            setLongEx(8, userEntity.seq)
        }

        return when (affectedRows) {
            1 -> userEntity
            else -> throw IncorrectResultSizeDataAccessException(1, affectedRows)
        }
    }

    private fun selectOne(sql: String, vararg args: Any): UserEntity? {
        val users = selectMany(sql, *args)

        return when (users.size) {
            0 -> null
            1 -> users[0]
            else -> throw IncorrectResultSizeDataAccessException(1, users.size)
        }
    }

    private fun selectMany(sql: String, vararg args: Any): List<UserEntity> =
        jdbcTemplate.queryForList(sql, *args).map { UserEntity.from(this, it) }
}
