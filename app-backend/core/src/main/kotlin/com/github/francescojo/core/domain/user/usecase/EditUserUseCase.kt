/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.domain.user.usecase

import com.github.francescojo.core.annotation.UseCase
import com.github.francescojo.core.domain.user.UserId
import com.github.francescojo.core.domain.user.exception.SameEmailUserAlreadyExistException
import com.github.francescojo.core.domain.user.exception.SameNicknameUserAlreadyExistException
import com.github.francescojo.core.domain.user.model.User
import com.github.francescojo.core.domain.user.projection.UserProjection
import com.github.francescojo.core.domain.user.projection.finder.UserProjectionFinder
import com.github.francescojo.core.domain.user.repository.UserRepository

/**
 * @since 2021-08-10
 */
interface EditUserUseCase {
    fun editUser(id: UserId, message: EditUserMessage): UserProjection

    interface EditUserMessage {
        val nickname: String?
        val email: String?
    }

    companion object {
        fun newInstance(
            userProjectionFinder: UserProjectionFinder,
            userRepository: UserRepository
        ): EditUserUseCase = EditUserUseCaseImpl(
            userFinder = userProjectionFinder,
            users = userRepository
        )
    }
}

@UseCase
internal class EditUserUseCaseImpl(
    private val userFinder: UserProjectionFinder,
    private val users: UserRepository
) : EditUserUseCase {
    override fun editUser(id: UserId, message: EditUserUseCase.EditUserMessage): UserProjection {
        val existingUser = userFinder.getById(id)

        message.nickname.takeIf { !it.isNullOrEmpty() }?.let { nickname ->
            users.findByNickname(nickname)?.let { throw SameNicknameUserAlreadyExistException(nickname) }
        }
        message.email.takeIf { !it.isNullOrEmpty() }?.let { email ->
            users.findByEmail(email)?.let { throw SameEmailUserAlreadyExistException(email) }
        }

        val modifiedUser = existingUser.mutate().applyValues(message)

        // region TODO: Transaction required
        users.save(User.from(modifiedUser))
        // endregion

        return modifiedUser
    }
}
