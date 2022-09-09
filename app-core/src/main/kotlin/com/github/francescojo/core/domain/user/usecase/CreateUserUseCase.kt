/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.domain.user.usecase

import com.github.francescojo.core.annotation.UseCase
import com.github.francescojo.core.domain.user.User
import com.github.francescojo.core.domain.user.UserObjectFactory
import com.github.francescojo.core.domain.user.exception.SameEmailUserAlreadyExistException
import com.github.francescojo.core.domain.user.exception.SameNicknameUserAlreadyExistException
import com.github.francescojo.core.domain.user.repository.UserRepository

/**
 * @since 2021-08-10
 */
interface CreateUserUseCase {
    fun createUser(message: CreateUserMessage): User

    interface CreateUserMessage {
        val nickname: String
        val email: String
    }

    companion object {
        fun newInstance(
            userRepository: UserRepository,
            userObjectFactory: UserObjectFactory
        ): CreateUserUseCase = CreateUserUseCaseImpl(
            userRepository,
            userObjectFactory
        )
    }
}

@UseCase
internal class CreateUserUseCaseImpl(
    private val users: UserRepository,
    private val objects: UserObjectFactory
) : CreateUserUseCase {
    override fun createUser(message: CreateUserUseCase.CreateUserMessage): User {
        users.findByNickname(message.nickname)?.let { throw SameNicknameUserAlreadyExistException(message.nickname) }
        users.findByEmail(message.email)?.let { throw SameEmailUserAlreadyExistException(message.email) }

        val user = objects.user(
            nickname = message.nickname,
            email = message.email
        )

        return users.save(user)
    }
}
