/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package testcase.large.endpoint.v1.user

import com.github.francescojo.core.domain.user.UserId
import com.github.francescojo.core.exception.ErrorCodes
import com.github.francescojo.lib.webApi.response.SimpleResponse
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import test.domain.user.UserTestUtils.random
import test.endpoint.v1.user.UserApiTestSupport.createRandomUser
import test.endpoint.v1.user.UserApiTestSupport.deleteUserApi
import testcase.large.endpoint.EndpointLargeTestBase

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
            UserId(createdUser.id),
            responseFields = null
        ).expect2xx(SimpleResponse::class)

        // expect:
        deleteResult.result shouldBe true
    }

    @DisplayName("Cannot delete user whom is not exist in server")
    @Test
    fun userInfoNotFound() {
        // expect:
        deleteUserApi(UserId.random())
            .expect4xx(HttpStatus.NOT_FOUND)
            .withExceptionCode(ErrorCodes.USER_BY_ID_NOT_FOUND)
    }
}
