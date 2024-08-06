/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package testcase.small.domain.user

import com.github.francescojo.core.domain.user.UserId
import com.github.francescojo.core.domain.user.exception.SameEmailUserAlreadyExistException
import com.github.francescojo.core.domain.user.exception.SameNicknameUserAlreadyExistException
import com.github.francescojo.core.domain.user.exception.UserByIdNotFoundException
import com.github.francescojo.core.domain.user.model.User
import com.github.francescojo.core.domain.user.projection.UserProjection
import com.github.francescojo.core.domain.user.projection.finder.UserProjectionFinder
import com.github.francescojo.core.domain.user.repository.UserRepository
import com.github.francescojo.core.domain.user.usecase.EditUserUseCase
import com.github.francescojo.lib.annotation.SmallTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import test.domain.user.EMPTY
import test.domain.user.random
import test.domain.user.randomEditUserMessage
import test.domain.user.randomUserProjection

/**
 * @since 2021-08-10
 */
@SmallTest
internal class EditUserUseCaseSpec {
    private lateinit var sut: EditUserUseCase
    private lateinit var userProjectionFinder: UserProjectionFinder
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setup() {
        userProjectionFinder = mock()
        userRepository = mock()
        sut = EditUserUseCase.newInstance(
            userProjectionFinder = userProjectionFinder,
            userRepository = userRepository
        )
    }

    @DisplayName("Error occurred if user with given id is not found")
    @Test
    fun errorIfUserNotFound() {
        // given:
        val id = UserId.random()

        // then:
        assertThrows<UserByIdNotFoundException> { sut.editUser(id, randomEditUserMessage()) }
    }

    @DisplayName("User info is updated by given message")
    @Test
    fun userIsUpdated() {
        // given:
        val id = UserId.random()
        val message = randomEditUserMessage()

        // and:
        userRepository.save(User.from(randomUserProjection(id = id)))

        // when:
        val editedUser = sut.editUser(id, message)

        // then:
        assertAll(
            { assertThat(editedUser.nickname, `is`(message.nickname)) },
            { assertThat(editedUser.email, `is`(message.email)) },
        )
    }

    @DisplayName("Fails if problems on some field, when:")
    @Nested
    inner class DuplicationErrorOccurred {
        private var id: UserId = UserId.EMPTY

        private lateinit var message: EditUserUseCase.EditUserMessage

        @BeforeEach
        fun setup() {
            // given:
            id = UserId.random()
            message = randomEditUserMessage()
        }

        @DisplayName("Nickname is duplicated")
        @Test
        fun errorIfNicknameIsDuplicated() {
            // given:
            val sameNicknameUser = randomUserProjection(id = id, nickname = message.nickname!!)

            // and:
            userRepository.save(User.from(sameNicknameUser))

            // then:
            assertThrows<SameNicknameUserAlreadyExistException> { sut.editUser(id, message) }
        }

        @DisplayName("Email is duplicated")
        @Test
        fun errorIfEmailIsDuplicated() {
            // given:
            val sameEmailUser = randomUserProjection(id = id, email = message.email!!)

            // and:
            userRepository.save(User.from(sameEmailUser))

            // then:
            assertThrows<SameEmailUserAlreadyExistException> { sut.editUser(id, message) }
        }
    }

    @DisplayName("Some field is preserved if:")
    @Nested
    inner class SomeFieldIsPreserved {
        private var id: UserId = UserId.EMPTY
        private lateinit var existingUser: UserProjection

        @BeforeEach
        fun setup() {
            // given:
            id = UserId.random()

            // and:
            existingUser = randomUserProjection(id = id)
            userRepository.save(User.from(existingUser))
        }

        @DisplayName("nickname is omitted in message")
        @Test
        fun nicknameIsPreserved() {
            // given:
            val message = randomEditUserMessage(nickname = null)

            // then:
            val updatedUser = sut.editUser(id, message)

            // expect:
            assertAll(
                { assertThat(updatedUser.nickname, `is`(existingUser.nickname)) },
                { assertThat(updatedUser.nickname, not(message.nickname)) },
                { assertThat(updatedUser.email, `is`(message.email)) },
            )
        }

        @DisplayName("email is omitted in message")
        @Test
        fun emailIsPreserved() {
            // given:
            val message = randomEditUserMessage(email = null)

            // then:
            val updatedUser = sut.editUser(id, message)

            // expect:
            assertAll(
                { assertThat(updatedUser.nickname, `is`(message.nickname)) },
                { assertThat(updatedUser.email, `is`(existingUser.email)) },
                { assertThat(updatedUser.email, not(message.email)) }
            )
        }
    }
}
