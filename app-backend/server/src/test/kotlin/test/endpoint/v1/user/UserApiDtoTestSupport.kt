/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package test.endpoint.v1.user

import com.github.francescojo.endpoint.v1.user.create.CreateUserRequest
import com.github.francescojo.endpoint.v1.user.edit.EditUserRequest
import com.github.francescojo.lib.util.toOptional
import test.com.github.francescojo.lib.SharedTestObjects.faker

/**
 * @since 2022-09-10
 */
object UserApiDtoTestSupport {
    fun CreateUserRequest.Companion.random(
        nickname: String = faker.name().fullName(),
        email: String = faker.internet().emailAddress()
    ) = CreateUserRequest(
        nickname = nickname,
        email = email
    )

    fun EditUserRequest.Companion.random(
        nickname: String? = faker.name().fullName(),
        email: String? = faker.internet().emailAddress()
    ) = EditUserRequest(
        nickname = nickname?.toOptional(),
        email = email?.toOptional()
    )
}
