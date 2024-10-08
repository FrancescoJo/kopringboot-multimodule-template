/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package testcase.medium.endpoint.v1.user

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.github.francescojo.core.domain.user.model.User
import com.github.francescojo.core.exception.ErrorCodes
import com.github.francescojo.endpoint.v1.ApiPathsV1
import io.kotest.matchers.shouldBe
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import test.com.github.francescojo.lib.SharedTestObjects.faker
import testcase.medium.ControllerMediumTestBase
import java.util.stream.Stream

/**
 * @since 2021-08-10
 */
class CreateUserRequestSpec : ControllerMediumTestBase() {
    @ParameterizedTest(name = "Fails if it is {0} characters")
    @MethodSource("badNicknames")
    fun failIfNicknamesAreBad(
        @Suppress("UNUSED_PARAMETER") testName: String,
        nickname: String
    ) {
        // given:
        val payload = FakeCreateUserRequest(
            nickname = nickname,
            email = faker.internet().emailAddress()
        )

        // when:
        val request = post(ApiPathsV1.USERS, payload)

        // then:
        val errorResponse = request.send().expect4xx()

        // expect:
        ErrorCodes.from(errorResponse.code) shouldBe ErrorCodes.WRONG_INPUT
    }

    @ParameterizedTest(name = "Fails if it is {0}")
    @MethodSource("badEmails")
    fun failIfEmailsAreBad(
        @Suppress("UNUSED_PARAMETER") testName: String,
        email: String
    ) {
        // given:
        val payload = FakeCreateUserRequest(
            nickname = faker.name().fullName(),
            email = email
        )

        // when:
        val request = post(ApiPathsV1.USERS, payload)

        // then:
        val errorResponse = request.send().expect4xx()

        // expect:
        ErrorCodes.from(errorResponse.code) shouldBe ErrorCodes.WRONG_INPUT
    }

    @JsonDeserialize
    private data class FakeCreateUserRequest(
        val nickname: String?,
        val email: String?
    )

    companion object {
        @JvmStatic
        fun badNicknames(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "shorter than ${User.LENGTH_NAME_MIN}",
                faker.letterify("?").repeat(User.LENGTH_NAME_MIN - 1),
            ),
            Arguments.of(
                "longer than ${User.LENGTH_NAME_MAX}",
                faker.letterify("?").repeat(User.LENGTH_NAME_MAX + 1),
            )
        )

        @JvmStatic
        fun badEmails(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "empty",
                "",
            ),
            Arguments.of(
                "not an IETF email format",
                faker.lorem().word(),
            ),
            Arguments.of(
                "longer than ${User.LENGTH_EMAIL_MAX}",
                faker.letterify("?").repeat(User.LENGTH_EMAIL_MAX + 1) + "@company.com",
            )
        )
    }
}
