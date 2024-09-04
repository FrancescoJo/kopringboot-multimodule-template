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
import com.github.francescojo.core.domain.user.usecase.EditUserUseCase
import com.github.francescojo.lib.annotation.SmallTest
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import test.domain.user.UserTestUtils.emptyUserId
import test.domain.user.UserTestUtils.randomUserId
import test.domain.user.UserTestUtils.randomUserProjection
import test.domain.user.UserUseCaseTestUtils.randomEditUserMessage
import test.domain.user.repository.MockUserRepository

/**
 * @since 2021-08-10
 */
@SmallTest
internal class EditUserUseCaseSpec {
    private lateinit var sut: EditUserUseCase
    private lateinit var userRepository: MockUserRepository

    @BeforeEach
    fun setup() {
        userRepository = MockUserRepository()
        sut = EditUserUseCase.newInstance(
            userRepository = userRepository
        )
    }

    @DisplayName("Error occurred if user with given id is not found")
    @Test
    fun errorIfUserNotFound() {
        // given:
        val id = randomUserId()

        // then:
        assertThrows<UserByIdNotFoundException> { sut.editUser(id, randomEditUserMessage()) }
    }

    @DisplayName("User info is updated by given message")
    @Test
    fun userIsUpdated() {
        // given:
        val id = randomUserId()
        val message = randomEditUserMessage()

        // and:
        userRepository.save(User.from(randomUserProjection(id = id)))

        // when:
        val editedUser = sut.editUser(id, message)

        // then:
        assertAll(
            { editedUser.nickname shouldBe message.nickname.get() },
            { editedUser.email shouldBe message.email.get() }
        )
    }

    @DisplayName("Fails if problems on some field, when:")
    @Nested
    inner class DuplicationErrorOccurred {
        private var id: UserId = emptyUserId()

        private lateinit var message: EditUserUseCase.EditUserMessage

        @BeforeEach
        fun setup() {
            // given:
            id = randomUserId()
            message = randomEditUserMessage()
        }

        @DisplayName("Nickname is duplicated")
        @Test
        fun errorIfNicknameIsDuplicated() {
            // given:
            val sameNicknameUser = randomUserProjection(id = id, nickname = message.nickname.get())

            // and:
            userRepository.save(User.from(sameNicknameUser))

            // then:
            assertThrows<SameNicknameUserAlreadyExistException> { sut.editUser(id, message) }
        }

        @DisplayName("Email is duplicated")
        @Test
        fun errorIfEmailIsDuplicated() {
            // given:
            val sameEmailUser = randomUserProjection(id = id, email = message.email.get())

            // and:
            userRepository.save(User.from(sameEmailUser))

            // then:
            assertThrows<SameEmailUserAlreadyExistException> { sut.editUser(id, message) }
        }
    }

    @DisplayName("Some field is preserved if:")
    @Nested
    inner class SomeFieldIsPreserved {
        private var id: UserId = emptyUserId()
        private lateinit var existingUser: UserProjection

        @BeforeEach
        fun setup() {
            // given:
            id = randomUserId()

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
                { updatedUser.nickname shouldBe existingUser.nickname },
                { updatedUser.email shouldBe message.email.get() }
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
                { updatedUser.nickname shouldBe message.nickname.get() },
                { updatedUser.email shouldBe existingUser.email }
            )
        }
    }
}
