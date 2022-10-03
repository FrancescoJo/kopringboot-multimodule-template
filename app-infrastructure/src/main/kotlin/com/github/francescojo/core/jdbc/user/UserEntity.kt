/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.jdbc.user

import com.github.francescojo.core.domain.user.User
import com.github.francescojo.core.jdbc.JdbcTemplateHelper
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
    val uuid: UUID,
    var nickname: String,
    var email: String,
    var registeredAt: Instant,
    var lastActiveAt: Instant,
    var deleted: Boolean
) {
    var seq: Long? = null

    var version: Long = 0L

    fun toUser(): User = User.create(
        id = this.uuid,
        nickname = this.nickname,
        email = this.email,
        registeredAt = this.registeredAt,
        lastActiveAt = this.lastActiveAt,
        deleted = this.deleted
    )

    override fun equals(other: Any?): Boolean = when {
        this === other -> true
        other !is UserEntity -> false
        else -> this.seq == other.seq
    }

    override fun hashCode(): Int = Objects.hash(this.seq)

    companion object {
        const val TABLE = "users"

        const val COL_SEQ = "seq"
        const val COL_UUID = "uuid"
        const val COL_NICKNAME = "nickname"
        const val COL_EMAIL = "email"
        const val COL_DELETED = "deleted"
        const val COL_CREATED_AT = "created_at"
        const val COL_UPDATED_AT = "updated_at"
        const val COL_VERSION = "version"

        fun from(user: User): UserEntity = with(user) {
            UserEntity(
                uuid = id,
                nickname = nickname,
                email = email,
                registeredAt = registeredAt,
                lastActiveAt = lastActiveAt,
                deleted = deleted
            )
        }

        fun from(
            deserialisationContext: JdbcTemplateHelper,
            map: Map<String, Any?>,
            prefix: String = ""
        ) = with(deserialisationContext) {
            UserEntity(
                uuid = (map[prefix + COL_UUID] as ByteArray).toUUID(),
                nickname = map[prefix + COL_NICKNAME] as String,
                email = map[prefix + COL_EMAIL] as String,
                registeredAt = map[prefix + COL_CREATED_AT]!!.coerceToInstant(),
                lastActiveAt = map[prefix + COL_UPDATED_AT]!!.coerceToInstant(),
                deleted = map[prefix + COL_DELETED] as Boolean
            ).apply {
                this.seq = map[prefix + COL_SEQ] as Long
                this.version = map[prefix + COL_VERSION] as Long
            }
        }
    }
}
