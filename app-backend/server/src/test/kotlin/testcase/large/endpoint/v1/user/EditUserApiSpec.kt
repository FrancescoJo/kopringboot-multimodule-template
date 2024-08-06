/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package testcase.large.endpoint.v1.user

import com.github.francescojo.core.exception.ErrorCodes
import com.github.francescojo.endpoint.v1.user.common.UserResponse
import com.github.francescojo.endpoint.v1.user.edit.EditUserRequest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.http.HttpStatus
import test.endpoint.v1.user.createRandomUser
import test.endpoint.v1.user.editUserApi
import test.endpoint.v1.user.random
import testcase.large.endpoint.EndpointLargeTestBase
import java.util.*
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
            createdUser.id,
            request,
            requestFields = null,
            responseFields = null
        ).expect2xx(UserResponse::class)

        // expect:
        assertThat(response, isReflecting = request)
    }

    @DisplayName("Cannot edit user whom is not exist in server")
    @Test
    fun userInfoNotFound() {
        // expect:
        editUserApi(UUID.randomUUID(), EditUserRequest.random())
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
            val editedUser = editUserApi(createdUser.id, request)
                .expect2xx(UserResponse::class)

            // then:
            assertThat(editedUser, isReflecting = request)
        }

        @ParameterizedTest(name = "Email is {0}")
        @MethodSource("testcase.large.endpoint.v1.user.EditUserApiSpec#emptyEmails")
        @Suppress("UNUSED_PARAMETER")
        fun emailIsOmitted(_testName: String, request: EditUserRequest) {
            // when:
            val editedUser = editUserApi(createdUser.id, request)
                .expect2xx(UserResponse::class)

            // then:
            assertThat(editedUser, isReflecting = request)
        }
    }

    @DisplayName("Cannot edit user if:")
    @Nested
    inner class CannotCreateUserIf {
        @DisplayName("Nickname is duplicated")
        @Test
        fun nicknameIsDuplicated() {
            // expect:
            editUserApi(createdUser.id, EditUserRequest.random(nickname = createdUser.nickname))
                .expect4xx(HttpStatus.CONFLICT)
                .withExceptionCode(ErrorCodes.USER_BY_NICKNAME_DUPLICATED)
        }

        @DisplayName("Email is duplicated")
        @Test
        fun emailIsDuplicated() {
            // expect:
            editUserApi(createdUser.id, EditUserRequest.random(email = createdUser.email))
                .expect4xx(HttpStatus.CONFLICT)
                .withExceptionCode(ErrorCodes.USER_BY_EMAIL_DUPLICATED)
        }
    }

    private fun assertThat(actual: UserResponse, isReflecting: EditUserRequest) {
        assertAll(
            {
                if (isReflecting.nickname != null) {
                    assertThat(actual.nickname, `is`(isReflecting.nickname))
                }
            },
            {
                if (isReflecting.email != null) {
                    assertThat(actual.email, `is`(isReflecting.email))
                }
            }
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
