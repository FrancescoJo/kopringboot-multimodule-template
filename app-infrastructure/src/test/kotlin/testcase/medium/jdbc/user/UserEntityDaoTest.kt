/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package testcase.medium.jdbc.user

import com.github.francescojo.core.jdbc.user.UserEntity
import com.github.francescojo.core.jdbc.user.dao.UserEntityDao
import com.github.javafaker.Faker
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.nullValue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import test.domain.user.randomUserEntity
import testcase.medium.JdbcTemplateMediumTestBase

/**
 * @since 2021-08-10
 */
class UserEntityDaoTest : JdbcTemplateMediumTestBase() {
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
        assertThat(savedUser.uuid, not(nullValue()))

        // then:
        val foundUser = sut.selectByUuid(savedUser.uuid)

        // expect:
        assertThat(foundUser, `is`(savedUser))
    }


    @DisplayName("Changes in entity must be persisted")
    @Test
    fun updateTest() {
        // given:
        val savedUser = sut.insert(randomUserEntity())

        // when:
        val newNickname = Faker().superhero().name()
        val newEmail = Faker().internet().safeEmailAddress()
        val foundUser = sut.selectByUuid(savedUser.uuid)!!.apply {
            this.nickname = newNickname
            this.email = newEmail
        }

        // then:
        val updatedUser = sut.update(foundUser.seq!!, foundUser)

        // then:
        val retrievedUser = sut.selectByUuid(updatedUser.uuid)!!

        // expect:
        assertAll(
            { assertThat(retrievedUser.nickname, `is`(newNickname)) },
            { assertThat(retrievedUser.email, `is`(newEmail)) },
        )
    }

    @DisplayName("Inserted user must be found for criteria:")
    @Nested
    inner class SavedUserMustBeFoundBy {
        private lateinit var savedUser: UserEntity

        @BeforeEach
        fun setup() {
            savedUser = sut.insert(randomUserEntity())
        }

        @DisplayName("uuid")
        @Test
        fun byUuid() {
            // when:
            val foundUser = sut.selectByUuid(savedUser.uuid)

            // then:
            assertThat(foundUser, `is`(savedUser))
        }

        @DisplayName("nickname")
        @Test
        fun byNickname() {
            // when:
            val foundUser = sut.selectByNickname(savedUser.nickname)

            // then:
            assertThat(foundUser, `is`(savedUser))
        }

        @DisplayName("email")
        @Test
        fun byEmail() {
            // when:
            val foundUser = sut.selectByEmail(savedUser.email)

            // then:
            assertThat(foundUser, `is`(savedUser))
        }
    }
}
