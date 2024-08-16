/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.domain.user.usecase

import com.github.francescojo.core.annotation.UseCase
import com.github.francescojo.core.domain.user.UserId
import com.github.francescojo.core.domain.user.exception.SameEmailUserAlreadyExistException
import com.github.francescojo.core.domain.user.exception.SameNicknameUserAlreadyExistException
import com.github.francescojo.core.domain.user.projection.UserProjection
import com.github.francescojo.core.domain.user.repository.UserRepository
import com.github.francescojo.lib.util.isUndefinedOrNull
import com.github.francescojo.lib.util.takeOr
import java.util.*

/**
 * @since 2021-08-10
 */
interface EditUserUseCase {
    fun editUser(id: UserId, message: EditUserMessage): UserProjection

    data class EditUserMessage(
        val nickname: Optional<String>,
        val email: Optional<String>
    )

    companion object {
        fun newInstance(
            userRepository: UserRepository
        ): EditUserUseCase = EditUserUseCaseImpl(
            users = userRepository
        )
    }
}

@UseCase
internal class EditUserUseCaseImpl(
    private val users: UserRepository
) : EditUserUseCase {
    override fun editUser(id: UserId, message: EditUserUseCase.EditUserMessage): UserProjection {
        val user = users.getById(id)

        message.nickname.takeIf { !it.isUndefinedOrNull() }?.get()?.let {
            if (users.findByNickname(it) != null) {
                throw SameNicknameUserAlreadyExistException(it)
            }
        }
        message.email.takeIf { !it.isUndefinedOrNull() }?.get()?.let {
            if (users.findByEmail(it) != null) {
                throw SameEmailUserAlreadyExistException(it)
            }
        }

        val modifiedUser = user.mutate().apply {
            this.nickname = message.nickname.takeOr { this.nickname }
            this.email = message.email.takeOr { this.email }
        }

        // region TODO: Transaction required
        users.save(modifiedUser)
        // endregion

        return UserProjection.aggregate(modifiedUser)
    }
}
