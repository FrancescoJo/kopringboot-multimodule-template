/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package test.domain.user.repository

import com.github.francescojo.core.domain.user.UserId
import com.github.francescojo.core.domain.user.model.User
import com.github.francescojo.core.domain.user.repository.UserRepository
import test.repository.AbstractMockWritableRepository

/**
 * @since 2024-08-07
 */
class MockUserRepository : AbstractMockWritableRepository<User, UserId>(), UserRepository {
    private val nicknameStore: MutableMap<String, User> = mutableMapOf()
    private val emailStore: MutableMap<String, User> = mutableMapOf()

    override fun onSave(model: User): User {
        nicknameStore[model.nickname] = model
        emailStore[model.email] = model

        return super.onSave(model)
    }

    override fun onClear() {
        nicknameStore.clear()
        emailStore.clear()

        super.onClear()
    }

    override fun findByNickname(nickname: String): User? = nicknameStore[nickname]

    override fun findByEmail(email: String): User? = emailStore[email]
}
