/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package testcase.large.endpoint.v1.user

import com.github.francescojo.core.exception.ErrorCodes
import com.github.francescojo.endpoint.v1.user.common.UserResponse
import com.github.francescojo.endpoint.v1.user.create.CreateUserRequest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.http.HttpStatus
import test.endpoint.v1.user.createRandomUser
import test.endpoint.v1.user.createUserApi
import test.endpoint.v1.user.random
import testcase.large.endpoint.EndpointLargeTestBase

/**
 * @since 2021-08-10
 */
class CreateUserApiSpec : EndpointLargeTestBase() {
    @DisplayName("User is created for valid request")
    @Test
    fun userCreated() {
        // given:
        val request = CreateUserRequest.random()

        // then:
        val response = createUserApi(
            request,
            requestFields = null,
            responseFields = null
        ).expect2xx(UserResponse::class)

        // expect:
        assertThat(response, isReflecting = request)
    }

    @DisplayName("Cannot create user if:")
    @Nested
    inner class CannotCreateUserIf {
        private lateinit var createdUser: UserResponse

        @BeforeEach
        fun setUp() {
            createdUser = createRandomUser()
        }

        @DisplayName("Nickname is duplicated")
        @Test
        fun nicknameIsDuplicated() {
            // expect:
            createUserApi(CreateUserRequest.random(nickname = createdUser.nickname))
                .expect4xx(HttpStatus.CONFLICT)
                .withExceptionCode(ErrorCodes.USER_BY_NICKNAME_DUPLICATED)
        }

        @DisplayName("Email is duplicated")
        @Test
        fun emailIsDuplicated() {
            // expect:
            createUserApi(CreateUserRequest.random(email = createdUser.email))
                .expect4xx(HttpStatus.CONFLICT)
                .withExceptionCode(ErrorCodes.USER_BY_EMAIL_DUPLICATED)
        }
    }

    private fun assertThat(actual: UserResponse, isReflecting: CreateUserRequest) {
        assertAll(
            { assertThat(actual.nickname, `is`(isReflecting.nickname)) },
            { assertThat(actual.email, `is`(isReflecting.email)) }
        )
    }
}
