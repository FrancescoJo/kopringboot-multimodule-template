/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package testcase.small.domain.user

import com.github.francescojo.core.domain.user.UserId
import com.github.francescojo.core.domain.user.usecase.FindUserUseCase
import com.github.francescojo.lib.annotation.SmallTest
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
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
        notFoundUser shouldBe null
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
        foundUser shouldNotBe null
    }
}
