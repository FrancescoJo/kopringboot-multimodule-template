/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package testcase.large.endpoint.v1.user

import com.github.francescojo.core.domain.user.UserId
import com.github.francescojo.core.exception.ErrorCodes
import com.github.francescojo.endpoint.v1.user.common.UserResponse
import com.github.francescojo.endpoint.v1.user.edit.EditUserRequest
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.http.HttpStatus
import test.domain.user.UserTestUtils.random
import test.endpoint.v1.user.UserApiDtoTestSupport.random
import test.endpoint.v1.user.UserApiTestSupport.createRandomUser
import test.endpoint.v1.user.UserApiTestSupport.editUserApi
import testcase.large.endpoint.EndpointLargeTestBase
import java.util.stream.Stream

/**
 * @since 2021-08-10
 */
class EditUserApiSpec : EndpointLargeTestBase() {
    private lateinit var createdUser: UserResponse

    @BeforeEach
    fun setUp() {
        createdUser = createRandomUser()
    }

    @DisplayName("Can edit user whom is already exist in server")
    @Test
    fun userCreated() {
        // given:
        val request = EditUserRequest.random()

        // then:
        val response = editUserApi(
            UserId(createdUser.id),
            request,
            requestFields = null,
            responseFields = null
        ).expect2xx(UserResponse::class)

        // expect:
        response shouldReflect request
    }

    @DisplayName("Cannot edit user whom is not exist in server")
    @Test
    fun userInfoNotFound() {
        // expect:
        editUserApi(UserId.random(), EditUserRequest.random())
            .expect4xx(HttpStatus.NOT_FOUND)
            .withExceptionCode(ErrorCodes.USER_BY_ID_NOT_FOUND)
    }

    @DisplayName("Some information field is preserved if:")
    @Nested
    inner class SomeInformationIsPreservedIf {
        @DisplayName("Nickname is omitted")
        @Test
        fun nicknameIsOmitted() {
            // given:
            val request = EditUserRequest.random(nickname = null)

            // when:
            val editedUser = editUserApi(UserId(createdUser.id), request)
                .expect2xx(UserResponse::class)

            // then:
            editedUser shouldReflect request
        }

        @ParameterizedTest(name = "Email is {0}")
        @MethodSource("testcase.large.endpoint.v1.user.EditUserApiSpec#emptyEmails")
        fun emailIsOmitted(
            @Suppress("UNUSED_PARAMETER") testName: String,
            request: EditUserRequest
        ) {
            // when:
            val editedUser = editUserApi(UserId(createdUser.id), request)
                .expect2xx(UserResponse::class)

            // then:
            editedUser shouldReflect request
        }
    }

    @DisplayName("Cannot edit user if:")
    @Nested
    inner class CannotCreateUserProjectionIf {
        @DisplayName("Nickname is duplicated")
        @Test
        fun nicknameIsDuplicated() {
            // expect:
            editUserApi(UserId(createdUser.id), EditUserRequest.random(nickname = createdUser.nickname))
                .expect4xx(HttpStatus.CONFLICT)
                .withExceptionCode(ErrorCodes.USER_BY_NICKNAME_DUPLICATED)
        }

        @DisplayName("Email is duplicated")
        @Test
        fun emailIsDuplicated() {
            // expect:
            editUserApi(UserId(createdUser.id), EditUserRequest.random(email = createdUser.email))
                .expect4xx(HttpStatus.CONFLICT)
                .withExceptionCode(ErrorCodes.USER_BY_EMAIL_DUPLICATED)
        }
    }

    private infix fun UserResponse.shouldReflect(expected: EditUserRequest) {
        assertAll(
            { expected.nickname.takeIf { it.isPresent }?.let { this.nickname shouldBe it.get() } },
            { expected.email.takeIf { it.isPresent }?.let { this.email shouldBe it.get() } }
        )
    }

    companion object {
        @JvmStatic
        fun emptyEmails(): Stream<Arguments> = Stream.of(
            Arguments.of("null", EditUserRequest.random(email = null)),
            Arguments.of("empty", EditUserRequest.random(email = ""))
        )
    }
}
