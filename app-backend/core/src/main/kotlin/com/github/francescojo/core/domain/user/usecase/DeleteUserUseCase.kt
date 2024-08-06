/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.domain.user.usecase

import com.github.francescojo.core.annotation.UseCase
import com.github.francescojo.core.domain.user.UserId
import com.github.francescojo.core.domain.user.repository.UserRepository

/**
 * @since 2021-08-10
 */
interface DeleteUserUseCase {
    fun deleteUserById(id: UserId): Boolean

    companion object {
        fun newInstance(
            userRepository: UserRepository
        ): DeleteUserUseCase = DeleteUserUseCaseImpl(
            userRepository
        )
    }
}

@UseCase
internal class DeleteUserUseCaseImpl(
    private val users: UserRepository
) : DeleteUserUseCase {
    override fun deleteUserById(id: UserId): Boolean {
        // region TODO: Transaction required
        val existingUser = users.getById(id)

        return users.deleteById(existingUser.id)
        // endregion
    }
}
