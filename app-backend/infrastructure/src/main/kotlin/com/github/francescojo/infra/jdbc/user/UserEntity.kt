/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.infra.jdbc.user

import com.github.francescojo.core.domain.user.UserId
import com.github.francescojo.core.domain.user.model.User
import com.github.francescojo.infra.jdbc.JdbcTemplateHelper
import com.github.francescojo.lib.util.toUUID
import java.time.Instant
import java.util.*

/**
 * [equals] and [hashCode] implementation is inspired by the article as follows:
 * [Martin Fowler's blog: EvansClassification](https://martinfowler.com/bliki/EvansClassification.html)
 *
 * @since 2021-08-10
 */
internal class UserEntity(
    val id: UUID,
    var nickname: String,
    var email: String,
    var createdAt: Instant,
    var updatedAt: Instant,
    var deleted: Boolean = false
) {
    var seq: Long? = null

    var version: Long = 0L

    fun toUser(): User = User.create(
        id = UserId(this.id),
        nickname = this.nickname,
        email = this.email,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )

    override fun equals(other: Any?): Boolean = when {
        this === other -> true
        other !is UserEntity -> false
        else -> this.id == other.id
    }

    override fun hashCode(): Int = Objects.hash(this.id)

    companion object {
        const val TABLE = "users"

        const val COL_SEQ = "seq"
        const val COL_ID = "id"
        const val COL_NICKNAME = "nickname"
        const val COL_EMAIL = "email"
        const val COL_DELETED = "deleted"
        const val COL_CREATED_AT = "created_at"
        const val COL_UPDATED_AT = "updated_at"
        const val COL_VERSION = "version"

        fun from(user: User): UserEntity = with(user) {
            UserEntity(
                id = id.value,
                nickname = nickname,
                email = email,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }

        fun from(
            deserialisationContext: JdbcTemplateHelper,
            map: Map<String, Any?>,
            prefix: String = ""
        ) = with(deserialisationContext) {
            UserEntity(
                id = (map[prefix + COL_ID] as ByteArray).toUUID(),
                nickname = map[prefix + COL_NICKNAME] as String,
                email = map[prefix + COL_EMAIL] as String,
                createdAt = map[prefix + COL_CREATED_AT]!!.coerceToInstant(),
                updatedAt = map[prefix + COL_UPDATED_AT]!!.coerceToInstant(),
                deleted = map[prefix + COL_DELETED] as Boolean
            ).apply {
                this.seq = map[prefix + COL_SEQ] as Long
                this.version = map[prefix + COL_VERSION] as Long
            }
        }
    }
}
