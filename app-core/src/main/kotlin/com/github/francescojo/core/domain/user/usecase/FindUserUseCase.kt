/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.domain.user.usecase

import com.github.francescojo.core.annotation.UseCase
import com.github.francescojo.core.domain.user.User
import com.github.francescojo.core.domain.user.exception.UserByIdNotFoundException
import com.github.francescojo.core.domain.user.repository.UserRepository
import java.util.*

/**
 * @since 2021-08-10
 */
interface FindUserUseCase {
    fun getUserById(id: UUID): User = findUserById(id) ?: throw UserByIdNotFoundException(id)

    fun findUserById(id: UUID): User?

    companion object {
        fun newInstance(
            userRepository: UserRepository
        ): FindUserUseCase = FindUserUseCaseImpl(
            userRepository
        )
    }
}

@UseCase
internal class FindUserUseCaseImpl(
    private val users: UserRepository
) : FindUserUseCase {
    override fun findUserById(id: UUID): User? {
        return users.findByUuid(id)
    }
}
