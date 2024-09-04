/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package testcase.large.endpoint.v1.user

import com.github.francescojo.core.domain.user.repository.UserRepository
import com.github.francescojo.core.exception.ErrorCodes
import com.github.francescojo.endpoint.v1.user.common.UserResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import testcase.large.endpoint.EndpointLargeTestBase

import static test.domain.user.UserRepositoryTestSupport.deleteUserById
import static test.domain.user.UserTestUtils.randomUserId
import static test.endpoint.v1.user.UserApiGroovyTestSupport.createRandomUserApi
import static test.endpoint.v1.user.UserApiGroovyTestSupport.getUserApi

/**
 * @since 2024-09-04
 */
class GetUserApiSpec extends EndpointLargeTestBase {
    def "Can retrieve user information whom is already exist in server"() {
        given:
        UserResponse createdUser = null
        createdUser = createRandomUserApi(this)

        when:
        final userInfo = expect2xx(getUserApi(this, createdUser.id), UserResponse.class).get()

        then:
        userInfo == createdUser

        cleanup:
        deleteUserById(userRepository, createdUser?.id)
    }

    def "Cannot retrieve user information whom is not exist in server"() {
        expect:
        expect4xx(getUserApi(this, randomUserId()), HttpStatus.NOT_FOUND)
                .withExceptionCode(ErrorCodes.USER_BY_ID_NOT_FOUND)
    }

    @Autowired
    private UserRepository userRepository
}
