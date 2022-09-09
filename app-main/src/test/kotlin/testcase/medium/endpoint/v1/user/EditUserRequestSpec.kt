/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package testcase.medium.endpoint.v1.user

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.github.francescojo.core.domain.user.User
import com.github.francescojo.core.exception.ErrorCodes
import com.github.francescojo.endpoint.v1.ApiPathsV1
import com.github.javafaker.Faker
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import test.endpoint.v1.usersId
import testcase.medium.ControllerMediumTestBase
import java.util.*
import java.util.stream.Stream

/**
 * @since 2021-08-10
 */
class EditUserRequestSpec : ControllerMediumTestBase() {
    @ParameterizedTest(name = "Fails if it is {0} characters")
    @MethodSource("badNicknames")
    fun failIfNicknamesAreBad(
        testName: String,
        nickname: String
    ) {
        // given:
        val payload = FakeEditUserRequest(
            nickname = nickname,
            email = Faker().internet().emailAddress()
        )

        // when:
        val request = patch(ApiPathsV1.usersId(UUID.randomUUID()), payload)

        // then:
        val errorResponse = request.send().expect4xx()

        // expect:
        assertThat(ErrorCodes.from(errorResponse.code), `is`(ErrorCodes.WRONG_INPUT))
    }

    @ParameterizedTest(name = "Fails if it is {0}")
    @MethodSource("badEmails")
    fun failIfEmailsAreBad(
        testName: String,
        email: String
    ) {
        // given:
        val payload = FakeEditUserRequest(
            nickname = Faker().name().fullName(),
            email = email
        )

        // when:
        val request = patch(ApiPathsV1.usersId(UUID.randomUUID()), payload)

        // then:
        val errorResponse = request.send().expect4xx()

        // expect:
        assertThat(ErrorCodes.from(errorResponse.code), `is`(ErrorCodes.WRONG_INPUT))
    }

    @ParameterizedTest(name = "Fails if all request fields are {0}")
    @MethodSource("nullOrEmptyFields")
    fun failsIfAllRequestFieldsAreEmpty(
        testName: String,
        payload: FakeEditUserRequest
    ) {
        val request = patch(ApiPathsV1.usersId(UUID.randomUUID()), payload)

        // then:
        val errorResponse = request.send().expect4xx()

        // expect:
        assertThat(ErrorCodes.from(errorResponse.code), `is`(ErrorCodes.WRONG_INPUT))
    }

    @JsonDeserialize
    data class FakeEditUserRequest(
        val nickname: String?,
        val email: String?
    )

    companion object {
        @JvmStatic
        fun badNicknames(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "shorter than ${User.LENGTH_NAME_MIN}",
                Faker().letterify("?").repeat(User.LENGTH_NAME_MIN - 1),
            ),
            Arguments.of(
                "longer than ${User.LENGTH_NAME_MAX}",
                Faker().letterify("?").repeat(User.LENGTH_NAME_MAX + 1),
            )
        )

        @JvmStatic
        fun badEmails(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "not an IETF email format",
                Faker().lorem().word(),
            ),
            Arguments.of(
                "longer than ${User.LENGTH_EMAIL_MAX}",
                Faker().letterify("?").repeat(User.LENGTH_EMAIL_MAX + 1) + "@company.com",
            )
        )

        @JvmStatic
        fun nullOrEmptyFields(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "empty",
                FakeEditUserRequest(
                    nickname = "",
                    email = ""
                ),
            ),
            Arguments.of(
                "null",
                FakeEditUserRequest(
                    nickname = null,
                    email = null
                ),
            ),
        )
    }
}
