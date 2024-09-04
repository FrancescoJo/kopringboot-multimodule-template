/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package testcase.large.endpoint.v1.user

import com.github.francescojo.core.exception.ErrorCodes
import com.github.francescojo.lib.webApi.response.SimpleResponse
import org.springframework.http.HttpStatus
import testcase.large.endpoint.EndpointLargeTestBase

import static test.domain.user.UserTestUtils.randomUserId
import static test.endpoint.v1.user.UserApiGroovyTestSupport.createRandomUserApi
import static test.endpoint.v1.user.UserApiGroovyTestSupport.deleteUserApi

/**
 * @since 2024-09-04
 */
class DeleteUserApiSpec extends EndpointLargeTestBase {
    def "Can delete user whom is already exist in server"() {
        given:
        final createdUser = createRandomUserApi(this)

        when:
        final deleteResult = expect2xx(deleteUserApi(
                this,
                createdUser.id
        ), SimpleResponse.class).get()

        then:
        deleteResult.result == true
    }

    def "Cannot delete user whom is not exist in server"() {
        expect4xx(deleteUserApi(this, randomUserId()), HttpStatus.NOT_FOUND)
                .withExceptionCode(ErrorCodes.USER_BY_ID_NOT_FOUND)
    }
}
