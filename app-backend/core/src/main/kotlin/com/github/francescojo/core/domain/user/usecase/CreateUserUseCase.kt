/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.domain.user.usecase

import com.github.francescojo.core.annotation.UseCase
import com.github.francescojo.core.domain.user.exception.SameEmailUserAlreadyExistException
import com.github.francescojo.core.domain.user.exception.SameNicknameUserAlreadyExistException
import com.github.francescojo.core.domain.user.model.User
import com.github.francescojo.core.domain.user.projection.UserProjection
import com.github.francescojo.core.domain.user.repository.UserRepository

/**
 * @since 2021-08-10
 */
interface CreateUserUseCase {
    fun createUser(message: CreateUserMessage): UserProjection

    interface CreateUserMessage {
        val nickname: String
        val email: String
    }

    companion object {
        fun newInstance(
            userRepository: UserRepository
        ): CreateUserUseCase = CreateUserUseCaseImpl(
            userRepository
        )
    }
}

@UseCase
internal class CreateUserUseCaseImpl(
    private val users: UserRepository
) : CreateUserUseCase {
    override fun createUser(message: CreateUserUseCase.CreateUserMessage): UserProjection {
        // TODO: reduce to a single query
        users.findByNickname(message.nickname)?.let { throw SameNicknameUserAlreadyExistException(message.nickname) }
        users.findByEmail(message.email)?.let { throw SameEmailUserAlreadyExistException(message.email) }

        val user = User.create(
            nickname = message.nickname,
            email = message.email
        )

        // region TODO: Transaction required
        users.save(user)
        // endregion

        return UserProjection.aggregate(user)
    }
}
