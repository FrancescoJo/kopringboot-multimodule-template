/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package testcase.large.endpoint.v1.user

import com.github.francescojo.core.domain.user.repository.UserRepository
import com.github.francescojo.core.exception.ErrorCodes
import com.github.francescojo.endpoint.v1.user.common.UserResponse
import com.github.francescojo.endpoint.v1.user.create.CreateUserRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import spock.lang.Unroll
import testcase.large.endpoint.EndpointLargeTestBase

import java.util.function.Function

import static test.domain.user.UserRepositoryTestSupport.deleteUserById
import static test.endpoint.v1.user.UserApiDtoGroovyTestSupport.randomCreateUserRequest
import static test.endpoint.v1.user.UserApiGroovyTestSupport.createRandomUserApi
import static test.endpoint.v1.user.UserApiGroovyTestSupport.createUserApi

/**
 * @since 2024-09-03
 */
class CreateUserApiSpec extends EndpointLargeTestBase {
    def "User is created for valid request"() {
        given:
        final request = randomCreateUserRequest()

        when:
        UserResponse response = null
        response = expect2xx(createUserApi(this, request), UserResponse.class).get()

        then:
        response.nickname == request.nickname
        response.email == request.email

        cleanup:
        deleteUserById(userRepository, response?.id)
    }

    @Unroll
    def "Cannot create duplicated user if #condition is duplicated"(
            final String condition,
            final Function<UserResponse, CreateUserRequest> requestProvider,
            final ErrorCodes errorCode
    ) {
        given:
        UserResponse createdUser = null
        createdUser = createRandomUserApi(this)

        expect:
        expect4xx(createUserApi(this, requestProvider.apply(createdUser)), HttpStatus.CONFLICT)
                .withExceptionCode(errorCode)

        cleanup:
        deleteUserById(userRepository, createdUser?.id)

        where:
        condition  | requestProvider                         | errorCode
        "Nickname" | { UserResponse user ->
            randomCreateUserRequest(nickname: user.nickname)
        }                                                    | ErrorCodes.USER_BY_NICKNAME_DUPLICATED
        "Email"    | { UserResponse user ->
            randomCreateUserRequest(email: user.email)
        }                                                    | ErrorCodes.USER_BY_EMAIL_DUPLICATED
    }

    @Autowired
    private UserRepository userRepository
}
