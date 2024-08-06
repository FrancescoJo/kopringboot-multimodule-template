/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package testcase.large.endpoint.v1.user

import com.github.francescojo.core.exception.ErrorCodes
import com.github.francescojo.endpoint.v1.user.common.UserResponse
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import test.endpoint.v1.user.createRandomUser
import test.endpoint.v1.user.getUserApi
import testcase.large.endpoint.EndpointLargeTestBase
import java.util.*

/**
 * @since 2021-08-10
 */
class GetUserApiSpec : EndpointLargeTestBase() {
    @DisplayName("Can retrieve user information whom is already exist in server")
    @Test
    fun userInfoRetrieved() {
        // given:
        val createdUser = createRandomUser()

        // then:
        val userInfo = getUserApi(
            createdUser.id,
            responseFields = null
        ).expect2xx(UserResponse::class)

        // expect:
        assertThat(userInfo, `is`(createdUser))
    }

    @DisplayName("Cannot retrieve user information whom is not exist in server")
    @Test
    fun userInfoNotFound() {
        // expect:
        getUserApi(UUID.randomUUID())
            .expect4xx(HttpStatus.NOT_FOUND)
            .withExceptionCode(ErrorCodes.USER_BY_ID_NOT_FOUND)
    }
}
