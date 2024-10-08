/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package testcase.small.domain.user

import com.github.francescojo.core.domain.user.UserId
import com.github.francescojo.core.domain.user.exception.UserByIdNotFoundException
import com.github.francescojo.core.domain.user.model.User
import com.github.francescojo.core.domain.user.usecase.DeleteUserUseCase
import com.github.francescojo.lib.annotation.SmallTest
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import test.domain.user.UserTestUtils.random
import test.domain.user.UserTestUtils.randomUserProjection
import test.domain.user.repository.MockUserRepository

/**
 * @since 2021-08-10
 */
@SmallTest
internal class DeleteUserUseCaseSpec {
    private lateinit var sut: DeleteUserUseCase
    private lateinit var userRepository: MockUserRepository

    @BeforeEach
    fun setup() {
        userRepository = MockUserRepository()
        sut = DeleteUserUseCase.newInstance(userRepository)
    }

    @DisplayName("null is returned if user with given id is not found")
    @Test
    fun nullIfUserIsNotFound() {
        // given:
        val id = UserId.random()

        // then:
        assertThrows<UserByIdNotFoundException> { sut.deleteUserById(id) }
    }

    @DisplayName("User is returned if user with given id is found")
    @Test
    fun returnsUserIfFound() {
        // given:
        val id = UserId.random()

        // and:
        userRepository.save(User.from(randomUserProjection(id = id)))

        // then:
        val deletedUser = sut.deleteUserById(id)

        // expect:
        deletedUser shouldNotBe null
    }
}
