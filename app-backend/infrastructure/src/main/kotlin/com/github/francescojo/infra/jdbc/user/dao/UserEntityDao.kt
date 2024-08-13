/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.infra.jdbc.user.dao

import com.github.francescojo.infra.jdbc.AbstractJdbcTemplateDao
import com.github.francescojo.infra.jdbc.user.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*

/**
 * @since 2021-08-10
 */
internal interface UserEntityDao {
    fun selectById(id: UUID): UserEntity?

    fun selectByNickname(nickname: String): UserEntity?

    fun selectByEmail(email: String): UserEntity?

    fun insert(userEntity: UserEntity): UserEntity

    fun update(id: UUID, userEntity: UserEntity): UserEntity

    fun deleteById(id: UUID): Boolean
}

@Repository
internal interface UserEntityJpaRepository : JpaRepository<UserEntity, UUID>

@Repository
internal class UserEntityDaoImpl(
    private val userEntityJpaDao: UserEntityJpaRepository,

    jdbcTemplate: JdbcTemplate
) : AbstractJdbcTemplateDao(jdbcTemplate), UserEntityDao {
    override fun selectById(id: UUID): UserEntity? {
        TODO("Not yet implemented")
    }

    override fun selectByNickname(nickname: String): UserEntity? {
        TODO("Not yet implemented")
    }

    override fun selectByEmail(email: String): UserEntity? {
        TODO("Not yet implemented")
    }

    override fun insert(userEntity: UserEntity): UserEntity {
        TODO("Not yet implemented")
    }

    override fun update(id: UUID, userEntity: UserEntity): UserEntity {
        TODO("Not yet implemented")
    }

    override fun deleteById(id: UUID): Boolean {
        TODO("Not yet implemented")
    }
}
