/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.domain.user.projection.finder

import com.github.francescojo.core.domain.user.UserId
import com.github.francescojo.core.domain.user.exception.UserByIdNotFoundException
import com.github.francescojo.core.domain.user.projection.UserProjection
import com.github.francescojo.core.projection.ProjectionFinder
import com.github.francescojo.lib.collection.assertSingleOrNull

/**
 * @since 2024-08-06
 */
interface UserProjectionFinder : ProjectionFinder<UserProjection, UserId> {
    override fun getById(id: UserId): UserProjection =
        findAllByIds(setOf(id)).assertSingleOrNull() ?: throw UserByIdNotFoundException(id)

    companion object {
        const val NAME = "com.github.francescojo.core.domain.user.UserProjectionFinder"
    }
}
