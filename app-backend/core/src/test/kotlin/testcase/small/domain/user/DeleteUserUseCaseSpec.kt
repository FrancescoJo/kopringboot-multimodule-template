/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package testcase.small.domain.user

import com.github.francescojo.core.domain.user.UserId
import com.github.francescojo.core.domain.user.exception.UserByIdNotFoundException
import com.github.francescojo.core.domain.user.model.User
import com.github.francescojo.core.domain.user.repository.UserRepository
import com.github.francescojo.core.domain.user.usecase.DeleteUserUseCase
import com.github.francescojo.lib.annotation.SmallTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import test.domain.user.random
import test.domain.user.randomUserProjection

/**
 * @since 2021-08-10
 */
@SmallTest
internal class DeleteUserUseCaseSpec {
    private lateinit var sut: DeleteUserUseCase
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setup() {
        userRepository = mock()
        sut = DeleteUserUseCase.newInstance(userRepository)

        `when`(userRepository.save(any())).thenAnswer { return@thenAnswer it.arguments[0] }
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
        assertThat(deletedUser, `is`(notNullValue()))
    }
}
