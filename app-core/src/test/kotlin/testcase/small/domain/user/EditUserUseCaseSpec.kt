/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package testcase.small.domain.user

import com.github.francescojo.core.domain.user.User
import com.github.francescojo.core.domain.user.exception.SameEmailUserAlreadyExistException
import com.github.francescojo.core.domain.user.exception.SameNicknameUserAlreadyExistException
import com.github.francescojo.core.domain.user.exception.UserByIdNotFoundException
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
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import test.domain.user.aggregate.randomUser
import test.domain.user.randomEditUserMessage
import java.util.*

/**
 * @since 2021-08-10
 */
@SmallTest
class EditUserUseCaseSpec {
    private lateinit var sut: EditUserUseCase
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setup() {
        userRepository = mock()
        sut = EditUserUseCase.newInstance(userRepository)

        `when`(userRepository.save(any())).thenAnswer { return@thenAnswer it.arguments[0] }
    }

    @DisplayName("Error occurred if user with given id is not found")
    @Test
    fun errorIfUserNotFound() {
        // given:
        val id = UUID.randomUUID()

        // and:
        `when`(userRepository.findByUuid(id)).thenReturn(null)

        // then:
        assertThrows<UserByIdNotFoundException> { sut.editUser(id, randomEditUserMessage()) }
    }

    @DisplayName("User info is updated by given message")
    @Test
    fun userIsUpdated() {
        // given:
        val id = UUID.randomUUID()
        val message = randomEditUserMessage()

        // and:
        `when`(userRepository.findByUuid(id)).thenReturn(randomUser())

        // when:
        val editedUser = sut.editUser(id, message)

        // then:
        assertAll(
            { assertThat(editedUser.nickname, `is`(message.nickname)) },
            { assertThat(editedUser.email, `is`(message.email)) },
        )
    }

    @DisplayName("Fails if some field is duplicated, when:")
    @Nested
    inner class DuplicationErrorOccurred {
        private lateinit var id: UUID
        private lateinit var message: EditUserUseCase.EditUserMessage
        private lateinit var existingUser: User

        @BeforeEach
        fun setup() {
            // given:
            id = UUID.randomUUID()
            message = randomEditUserMessage()
            existingUser = randomUser(id = id)

            // and:
            `when`(userRepository.findByUuid(id)).thenReturn(existingUser)
        }

        @DisplayName("Nickname is duplicated")
        @Test
        fun errorIfNicknameIsDuplicated() {
            // given:
            val sameNicknameUser = randomUser(id = id, nickname = message.nickname!!)

            // and:
            `when`(userRepository.findByNickname(message.nickname!!)).thenReturn(sameNicknameUser)

            // then:
            assertThrows<SameNicknameUserAlreadyExistException> { sut.editUser(id, message) }
        }

        @DisplayName("Email is duplicated")
        @Test
        fun errorIfEmailIsDuplicated() {
            // given:
            val sameEmailUser = randomUser(id = id, email = message.email!!)

            // and:
            `when`(userRepository.findByEmail(message.email!!)).thenReturn(sameEmailUser)

            // then:
            assertThrows<SameEmailUserAlreadyExistException> { sut.editUser(id, message) }
        }
    }

    @DisplayName("Some field is preserved if:")
    @Nested
    inner class SomeFieldIsPreserved {
        private lateinit var id: UUID
        private lateinit var existingUser: User

        @BeforeEach
        fun setup() {
            // given:
            id = UUID.randomUUID()

            // and:
            existingUser = randomUser(id = id)
            `when`(userRepository.findByUuid(id)).thenReturn(existingUser)
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
