/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.domain.user.usecase

import com.github.francescojo.core.annotation.UseCase
import com.github.francescojo.core.domain.user.User
import com.github.francescojo.core.domain.user.aggregate.UserModel
import com.github.francescojo.core.domain.user.exception.SameEmailUserAlreadyExistException
import com.github.francescojo.core.domain.user.exception.SameNicknameUserAlreadyExistException
import com.github.francescojo.core.domain.user.exception.UserByIdNotFoundException
import com.github.francescojo.core.domain.user.repository.UserRepository
import java.util.*

/**
 * @since 2021-08-10
 */
interface EditUserUseCase {
    fun editUser(id: UUID, message: EditUserMessage): User

    interface EditUserMessage {
        val nickname: String?
        val email: String?
    }

    companion object {
        fun newInstance(
            userRepository: UserRepository
        ): EditUserUseCase = EditUserUseCaseImpl(
            userRepository
        )
    }
}

@UseCase
internal class EditUserUseCaseImpl(
    private val users: UserRepository
) : EditUserUseCase {
    override fun editUser(id: UUID, message: EditUserUseCase.EditUserMessage): User {
        val existingUser = users.findByUuid(id) ?: throw UserByIdNotFoundException(id)

        message.nickname.takeIf { !it.isNullOrEmpty() }?.let { nickname ->
            users.findByNickname(nickname)?.let { throw SameNicknameUserAlreadyExistException(nickname) }
        }
        message.email.takeIf { !it.isNullOrEmpty() }?.let { email ->
            users.findByEmail(email)?.let { throw SameEmailUserAlreadyExistException(email) }
        }

        return users.save(UserModel.from(existingUser).applyValues(message))
    }
}
