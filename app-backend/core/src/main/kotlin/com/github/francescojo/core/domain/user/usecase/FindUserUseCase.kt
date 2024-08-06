/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.domain.user.usecase

import com.github.francescojo.core.annotation.UseCase
import com.github.francescojo.core.domain.user.UserId
import com.github.francescojo.core.domain.user.projection.UserProjection
import com.github.francescojo.core.domain.user.projection.finder.UserProjectionFinder

/**
 * @since 2021-08-10
 */
interface FindUserUseCase {
    fun getUserById(id: UserId): UserProjection

    fun findUserById(id: UserId): UserProjection?

    companion object {
        fun newInstance(
            userProjectionFinder: UserProjectionFinder
        ): FindUserUseCase = FindUserUseCaseImpl(
            users = userProjectionFinder
        )
    }
}

@UseCase
internal class FindUserUseCaseImpl(
    private val users: UserProjectionFinder
) : FindUserUseCase {
    override fun getUserById(id: UserId): UserProjection {
        // region TODO: Transaction required
        return users.getById(id)
        // endregion
    }

    override fun findUserById(id: UserId): UserProjection? {
        // region TODO: Transaction required
        return users.findById(id)
        // endregion
    }
}
