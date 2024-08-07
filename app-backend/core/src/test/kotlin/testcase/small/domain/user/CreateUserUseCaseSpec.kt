/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package testcase.small.domain.user

import com.github.francescojo.core.domain.user.exception.SameEmailUserAlreadyExistException
import com.github.francescojo.core.domain.user.exception.SameNicknameUserAlreadyExistException
import com.github.francescojo.core.domain.user.model.User
import com.github.francescojo.core.domain.user.repository.UserRepository
import com.github.francescojo.core.domain.user.usecase.CreateUserUseCase
import com.github.francescojo.lib.annotation.SmallTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import test.domain.user.randomCreateUserMessage
import test.domain.user.randomUserProjection
import test.domain.user.repository.MockUserRepository

/**
 * @since 2021-08-10
 */
@SmallTest
internal class CreateUserUseCaseSpec {
    private lateinit var sut: CreateUserUseCase
    private lateinit var userRepository: MockUserRepository

    @BeforeEach
    fun setup() {
        userRepository = MockUserRepository()
        sut = CreateUserUseCase.newInstance(userRepository)
    }

    @AfterEach
    fun tearDown() {
        userRepository.clearMocks()
    }

    @DisplayName("An user object that fully represents message, is created")
    @Test
    fun userIsCreated() {
        // given:
        val message = randomCreateUserMessage()

        // when:
        val createdUser = sut.createUser(message)

        // then:
        assertAll(
            { assertThat(createdUser.nickname, `is`(message.nickname)) },
            { assertThat(createdUser.email, `is`(message.email)) },
        )
    }

    @DisplayName("Nickname must not be duplicated")
    @Test
    fun errorIfNicknameIsDuplicated() {
        // given:
        val message = randomCreateUserMessage()

        // and:
        userRepository.save(User.from(randomUserProjection(nickname = message.nickname)))

        // then:
        assertThrows<SameNicknameUserAlreadyExistException> { sut.createUser(message) }
    }

    @DisplayName("Email must not be duplicated")
    @Test
    fun errorIfEmailIsDuplicated() {
        // given:
        val message = randomCreateUserMessage()

        // and:
        userRepository.save(User.from(randomUserProjection(email = message.email)))

        // then:
        assertThrows<SameEmailUserAlreadyExistException> { sut.createUser(message) }
    }
}
