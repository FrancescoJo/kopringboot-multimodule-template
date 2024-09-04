/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package testcase.large.endpoint.v1.user

import com.github.francescojo.core.domain.user.repository.UserRepository
import com.github.francescojo.core.exception.ErrorCodes
import com.github.francescojo.endpoint.v1.user.common.UserResponse
import com.github.francescojo.endpoint.v1.user.edit.EditUserRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import spock.lang.Unroll
import testcase.large.endpoint.EndpointLargeTestBase

import static test.domain.user.UserRepositoryTestSupport.deleteUserById
import static test.domain.user.UserTestUtils.randomUserId
import static test.endpoint.v1.user.UserApiDtoGroovyTestSupport.randomEditUserRequest
import static test.endpoint.v1.user.UserApiGroovyTestSupport.createRandomUserApi
import static test.endpoint.v1.user.UserApiGroovyTestSupport.editUserApi

/**
 * @since 2024-09-04
 */
class EditUserApiSpec extends EndpointLargeTestBase {
    private UserResponse createdUser = null

    def setup() {
        createdUser = createRandomUserApi(this)
    }

    def cleanup() {
        deleteUserById(userRepository, createdUser?.id)
    }

    def "Can edit user whom is already exist in server"() {
        given:
        final request = randomEditUserRequest()

        when:
        final editedUser = expect2xx(editUserApi(
                this,
                createdUser.id,
                request
        ), UserResponse.class).get()

        then:
        shouldMatch(editedUser, request)
    }

    def "Cannot edit user whom is not exist in server"() {
        expect:
        expect4xx(editUserApi(this, randomUserId(), randomEditUserRequest()), HttpStatus.NOT_FOUND)
                .withExceptionCode(ErrorCodes.USER_BY_ID_NOT_FOUND)
    }

    @Unroll
    def "#fieldName is preserved if omitted in request"(final String fieldName) {
        given:
        final request = randomEditUserRequest((fieldName): null)

        when:
        final editedUser = expect2xx(editUserApi(
                this,
                createdUser.id,
                request
        ), UserResponse.class).get()

        then:
        switch (fieldName) {
            case "nickname":
                editedUser.nickname == createdUser.nickname
                editedUser.email == request.email.get()
                break
            case "email":
                editedUser.nickname == request.nickname.get()
                editedUser.email == createdUser.email
                break
        }

        expect:
        shouldMatch(editedUser, request)

        where:
        fieldName << ["nickname", "email"]
    }

    @Unroll
    def "Cannot edit if #fieldName is duplicated"(
            final String fieldName,
            final ErrorCodes errorCode
    ) {
        given:
        final request = randomEditUserRequest((fieldName): createdUser[fieldName])

        expect:
        expect4xx(editUserApi(this, createdUser.id, request), HttpStatus.CONFLICT)
                .withExceptionCode(errorCode)

        where:
        fieldName  | errorCode
        "nickname" | ErrorCodes.USER_BY_NICKNAME_DUPLICATED
        "email"    | ErrorCodes.USER_BY_EMAIL_DUPLICATED
    }

    private void shouldMatch(final UserResponse editedUser, final EditUserRequest request) {
        with(editedUser) {
            nickname == request.nickname.orElse(nickname)
            email == request.email.orElse(email)
        }
    }

    @Autowired
    private UserRepository userRepository
}
