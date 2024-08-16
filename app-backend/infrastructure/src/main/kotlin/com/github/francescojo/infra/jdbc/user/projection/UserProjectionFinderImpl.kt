/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.infra.jdbc.user.projection

import com.github.francescojo.core.domain.user.UserId
import com.github.francescojo.core.domain.user.projection.UserProjection
import com.github.francescojo.core.domain.user.projection.finder.UserProjectionFinder
import com.github.francescojo.core.domain.user.repository.UserRepository
import com.github.francescojo.infra.projection.AbstractProjectionFinderTemplate
import org.springframework.stereotype.Service

/**
 * @since 2024-08-06
 */
@Service(UserProjectionFinder.NAME)
class UserProjectionFinderImpl(
    private val userRepository: UserRepository
) : AbstractProjectionFinderTemplate<UserProjection, UserId>(), UserProjectionFinder {
    override fun findAllByIds(ids: Set<UserId>): List<UserProjection> {
        val users = userRepository.findAllByIds(ids)

        return users.map { UserProjection.aggregate(it) }
    }
}
