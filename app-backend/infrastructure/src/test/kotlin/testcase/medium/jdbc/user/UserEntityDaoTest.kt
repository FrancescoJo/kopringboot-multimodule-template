/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package testcase.medium.jdbc.user

import com.github.francescojo.core.domain.user.model.User
import com.github.francescojo.infra.jdbc.user.UserEntity
import com.github.francescojo.infra.jdbc.user.dao.UserEntityDao
import com.github.javafaker.Faker
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import test.domain.user.randomUserProjection
import testcase.medium.JdbcTemplateMediumTestBase

/**
 * @since 2021-08-10
 */
internal class UserEntityDaoTest : JdbcTemplateMediumTestBase() {
    @Autowired
    private lateinit var sut: UserEntityDao

    @DisplayName("Inserted entity must has an unique id")
    @Test
    fun insertTest() {
        // given:
        val randomUser = randomUserEntity()

        // then:
        val savedUser = sut.insert(randomUser)

        // expect:
        savedUser.id shouldNotBe null

        // then:
        val foundUser = sut.selectById(savedUser.id)

        // expect:
        foundUser shouldBe savedUser
    }

    @DisplayName("Changes in entity must be persisted")
    @Test
    fun updateTest() {
        // given:
        val savedUser = sut.insert(randomUserEntity())

        // when:
        val newNickname = Faker().superhero().name()
        val newEmail = Faker().internet().safeEmailAddress()
        val foundUser = sut.selectById(savedUser.id)!!.apply {
            this.nickname = newNickname
            this.email = newEmail
        }

        // then:
        val updatedUser = sut.update(foundUser.id, foundUser)

        // then:
        val retrievedUser = sut.selectById(updatedUser.id)!!

        // expect:
        assertAll(
            { retrievedUser.nickname shouldBe newNickname },
            { retrievedUser.email shouldBe newEmail },
        )
    }

    @DisplayName("Inserted user must be found for criteria:")
    @Nested
    inner class SavedUserProjectionMustBeFoundBy {
        private lateinit var savedUser: UserEntity

        @BeforeEach
        fun setup() {
            savedUser = sut.insert(randomUserEntity())
        }

        @DisplayName("uuid")
        @Test
        fun byUuid() {
            // when:
            val foundUser = sut.selectById(savedUser.id)

            // then:
            foundUser shouldBe savedUser
        }

        @DisplayName("nickname")
        @Test
        fun byNickname() {
            // when:
            val foundUser = sut.selectByNickname(savedUser.nickname)

            // then:
            foundUser shouldBe savedUser
        }

        @DisplayName("email")
        @Test
        fun byEmail() {
            // when:
            val foundUser = sut.selectByEmail(savedUser.email)

            // then:
            foundUser shouldBe savedUser
        }
    }

    private fun randomUserEntity(): UserEntity = UserEntity.from(User.from(randomUserProjection()))
}
