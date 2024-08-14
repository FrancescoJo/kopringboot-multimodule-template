/*
 * kopringboot-multimodule-monorepo-template
 * Sir.LOIN Intellectual property. All rights reserved.
 */
package com.github.francescojo.infra.jdbc.user.repository

import com.github.francescojo.core.domain.user.UserId
import com.github.francescojo.core.domain.user.model.User
import com.github.francescojo.infra.jdbc.common.embedded.DateEmbedded
import com.github.francescojo.infra.jdbc.user.UserEntity
import io.hypersistence.tsid.TSID

/**
 * @since 2024-08-13
 */
interface UserRepositorySupport {
    fun User.toUserEntity(): UserEntity = UserEntity(
        id = id.value.toLong(),
        nickname = nickname,
        email = email,
        timestamp = DateEmbedded(createdAt = createdAt, updatedAt = updatedAt)
    )

    fun UserEntity.toUser(): User = User.create(
        id = UserId(TSID(id)),
        nickname = nickname,
        email = email,
        createdAt = timestamp.createdAt,
        updatedAt = timestamp.updatedAt
    )
}
