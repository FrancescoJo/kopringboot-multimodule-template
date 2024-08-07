/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package testcase.small.domain.user

import com.github.francescojo.core.domain.user.UserId
import com.github.francescojo.core.domain.user.projection.finder.UserProjectionFinder
import com.github.francescojo.core.domain.user.usecase.FindUserUseCase
import com.github.francescojo.lib.annotation.SmallTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import test.domain.user.projection.MockUserProjectionFinder
import test.domain.user.random
import test.domain.user.randomUserProjection

/**
 * @since 2021-08-10
 */
@SmallTest
internal class FindUserUseCaseSpec {
    private lateinit var sut: FindUserUseCase
    private lateinit var userProjectionFinder: MockUserProjectionFinder

    @BeforeEach
    fun setup() {
        userProjectionFinder = MockUserProjectionFinder()
        sut = FindUserUseCase.newInstance(userProjectionFinder)
    }

    @AfterEach
    fun tearDown() {
        userProjectionFinder.clearMocks()
    }

    @DisplayName("null is returned if user with given id is not found")
    @Test
    fun nullIfUserIsNotFound() {
        // given:
        val id = UserId.random()

        // then:
        val notFoundUser = sut.findUserById(id)

        // expect:
        assertThat(notFoundUser, `is`(nullValue()))
    }

    @DisplayName("User is returned if user with given id is found")
    @Test
    fun returnsUserIfFound() {
        // given:
        val id = UserId.random()

        // and:
        userProjectionFinder.save(randomUserProjection(id = id))

        // then:
        val foundUser = sut.findUserById(id)

        // expect:
        assertThat(foundUser, not(nullValue()))
    }
}
