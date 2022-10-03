/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package testcase.small.domain.user

import com.github.francescojo.core.domain.user.repository.UserRepository
import com.github.francescojo.core.domain.user.usecase.FindUserUseCase
import com.github.francescojo.lib.annotation.SmallTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import test.domain.user.aggregate.randomUser
import java.util.*

/**
 * @since 2021-08-10
 */
@SmallTest
class FindUserUseCaseSpec {
    private lateinit var sut: FindUserUseCase
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setup() {
        userRepository = mock()
        sut = FindUserUseCase.newInstance(userRepository)

        `when`(userRepository.save(any())).thenAnswer { return@thenAnswer it.arguments[0] }
    }

    @DisplayName("null is returned if user with given id is not found")
    @Test
    fun nullIfUserIsNotFound() {
        // given:
        val id = UUID.randomUUID()

        // and:
        `when`(userRepository.findByUuid(id)).thenReturn(null)

        // then:
        val notFoundUser = sut.findUserById(id)

        // expect:
        assertThat(notFoundUser, `is`(nullValue()))
    }

    @DisplayName("User is returned if user with given id is found")
    @Test
    fun returnsUserIfFound() {
        // given:
        val id = UUID.randomUUID()

        // and:
        `when`(userRepository.findByUuid(id)).thenReturn(randomUser(id = id))

        // then:
        val foundUser = sut.findUserById(id)

        // expect:
        assertThat(foundUser, not(nullValue()))
    }
}
