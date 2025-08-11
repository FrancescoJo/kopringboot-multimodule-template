/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.domain.user.model.impl

import com.github.francescojo.core.domain.user.UserId
import com.github.francescojo.core.domain.user.model.User
import com.github.francescojo.lib.annotation.ValueParameter
import com.github.francescojo.lib.text.ToStringHelper.prettyToString
import java.time.Instant
import java.util.Objects

/**
 * @since 2021-08-10
 */
class UserMutator(
    @ValueParameter override val id: UserId,
    @ValueParameter nickname: String,
    @ValueParameter email: String,
    @ValueParameter override var createdAt: Instant,
    @ValueParameter override var updatedAt: Instant
) : User.Mutator {
    private var _nickname: String = nickname
    private var _email: String = email

    override var nickname: String
        get() = _nickname
        set(value) {
            _nickname = value
            updatedAt = Instant.now()
        }

    override var email: String
        get() = _email
        set(value) {
            _email = value
            updatedAt = Instant.now()
        }

    override fun equals(other: Any?): Boolean {
        return other is UserMutator &&
                id == other.id &&
                nickname == other.nickname &&
                email == other.email &&
                Objects.equals(createdAt, other.createdAt) &&
                Objects.equals(updatedAt, other.updatedAt)
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + updatedAt.hashCode()
        result = 31 * result + _nickname.hashCode()
        result = 31 * result + _email.hashCode()
        return result
    }

    override fun toString(): String = prettyToString(
        toStringTarget = this,
        attributes = mapOf(
            ::id.name to id,
            ::nickname.name to nickname,
            ::email.name to email,
            ::createdAt.name to createdAt,
            ::updatedAt.name to updatedAt
        )
    )

    companion object {
        fun from(src: User): User.Mutator = UserMutator(
            id = src.id,
            nickname = src.nickname,
            email = src.email,
            createdAt = src.createdAt,
            updatedAt = src.updatedAt
        )
    }
}
