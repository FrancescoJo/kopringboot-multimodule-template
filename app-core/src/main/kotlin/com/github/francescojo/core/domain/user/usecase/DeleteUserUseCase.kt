/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.domain.user.usecase

import com.github.francescojo.core.annotation.Usecase
import com.github.francescojo.core.domain.user.User
import com.github.francescojo.core.domain.user.exception.UserByIdNotFoundException
import com.github.francescojo.core.domain.user.repository.writable.UserRepository
import com.github.francescojo.core.domain.user.vo.UserModel
import java.util.*

/**
 * @since 2021-08-10
 */
interface DeleteUserUseCase {
    fun deleteUserById(id: UUID): User

    companion object {
        fun newInstance(
            userRepository: UserRepository
        ): DeleteUserUseCase = DeleteUserUseCaseImpl(
            userRepository
        )
    }
}

@Usecase
internal class DeleteUserUseCaseImpl(
    private val users: UserRepository
) : DeleteUserUseCase {
    override fun deleteUserById(id: UUID): User {
        val existingUser = users.findByUuid(id) ?: throw UserByIdNotFoundException(id)

        return users.save(UserModel.from(existingUser).delete())
    }
}
