/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package testcase.medium.jdbc.user

import com.github.francescojo.core.domain.user.model.User
import com.github.francescojo.core.domain.user.repository.UserRepository
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import test.domain.user.UserTestUtils.randomUserProjection
import testcase.medium.JdbcTemplateMediumTestBase
import java.util.stream.Stream

/**
 * @since 2021-08-10
 */
internal class UserRepositoryTest : JdbcTemplateMediumTestBase() {
    @Qualifier(UserRepository.NAME)
    @Autowired
    private lateinit var sut: UserRepository

    @ParameterizedTest(name = "We can find a saved User by {0}")
    @MethodSource("testcase.medium.jdbc.user.UserRepositoryTest#findSavedUserByParams")
    fun findSavedUserBy(
        @Suppress("UNUSED_PARAMETER") testName: String,
        findOperation: (UserRepository, User) -> User?
    ) {
        // given:
        val savedUser = sut.save(User.from(randomUserProjection()))

        // then:
        val foundUser = findOperation(sut, savedUser)

        // expect:
        foundUser shouldBe savedUser
    }

    @DisplayName("Changes in user is well preserved")
    @Test
    fun changesAreWellPreserved() {
        // given:
        val oldUser = sut.save(User.from(randomUserProjection()))

        // and:
        val newUser = sut.save(User.from(randomUserProjection(id = oldUser.id)))

        // then:
        val foundUser = sut.findById(oldUser.id)

        // expect:
        assertAll(
            { foundUser shouldBe newUser },
            { foundUser shouldNotBe oldUser }
        )
    }

    @DisplayName("Cannot find a deleted User")
    @Test
    fun cannotFindDeletedUser() {
        // given:
        val oldUser = sut.save(User.from(randomUserProjection()))

        // when:
        val deletedCount = sut.deleteById(oldUser.id)

        // then:
        val deletedUser = sut.findById(oldUser.id)

        // expect:
        assertAll(
            { deletedCount shouldBe true },
            { deletedUser shouldBe null }
        )
    }

    companion object {
        @JvmStatic
        fun findSavedUserByParams(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "its ID",
                { sut: UserRepository, user: User -> sut.findById(user.id) }
            ),
            Arguments.of(
                "its Email",
                { sut: UserRepository, user: User -> sut.findByEmail(user.email) }
            ),
            Arguments.of(
                "its Nickname",
                { sut: UserRepository, user: User -> sut.findByNickname(user.nickname) }
            )
        )
    }
}
