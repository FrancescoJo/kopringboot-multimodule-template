/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package test.domain.user

import com.github.francescojo.core.domain.user.usecase.CreateUserUseCase
import com.github.francescojo.core.domain.user.usecase.EditUserUseCase
import com.github.francescojo.lib.util.toOptional
import com.github.javafaker.Faker

/**
 * @since 2021-08-10
 */
object UserUseCaseTestUtils {
    fun randomCreateUserMessage(
        nickname: String = Faker().name().fullName(),
        email: String = Faker().internet().emailAddress()
    ): CreateUserUseCase.CreateUserMessage {
        data class FakeCreateUserMessage(
            override val nickname: String,
            override val email: String
        ) : CreateUserUseCase.CreateUserMessage

        return FakeCreateUserMessage(
            nickname = nickname,
            email = email
        )
    }

    fun randomEditUserMessage(
        nickname: String? = Faker().name().fullName(),
        email: String? = Faker().internet().emailAddress()
    ): EditUserUseCase.EditUserMessage {
        return EditUserUseCase.EditUserMessage(
            nickname = nickname?.toOptional(),
            email = email?.toOptional()
        )
    }
}
