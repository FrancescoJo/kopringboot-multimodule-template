/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package testcase.large.endpoint.v1.user

import com.github.francescojo.core.exception.ErrorCodes
import com.github.francescojo.endpoint.common.response.SimpleResponse
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import test.endpoint.v1.user.createRandomUser
import test.endpoint.v1.user.deleteUserApi
import testcase.large.endpoint.EndpointLargeTestBase
import java.util.*

/**
 * @since 2021-08-10
 */
class DeleteUserApiSpec : EndpointLargeTestBase() {
    @DisplayName("Can delete user whom is already exist in server")
    @Test
    fun userInfoRetrieved() {
        // given:
        val createdUser = createRandomUser()

        // then:
        val deleteResult = deleteUserApi(
            createdUser.id,
            responseFields = null
        ).expect2xx(SimpleResponse::class)

        // expect:
        assertThat(deleteResult.result, `is`(true))
    }

    @DisplayName("Cannot delete user whom is not exist in server")
    @Test
    fun userInfoNotFound() {
        // expect:
        deleteUserApi(UUID.randomUUID())
            .expect4xx(HttpStatus.NOT_FOUND)
            .withExceptionCode(ErrorCodes.USER_BY_ID_NOT_FOUND)
    }
}
