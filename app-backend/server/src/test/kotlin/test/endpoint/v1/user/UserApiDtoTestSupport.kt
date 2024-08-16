/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package test.endpoint.v1.user

import com.github.francescojo.endpoint.v1.user.create.CreateUserRequest
import com.github.francescojo.endpoint.v1.user.edit.EditUserRequest
import com.github.francescojo.lib.util.toOptional
import com.github.javafaker.Faker
import java.util.*

fun CreateUserRequest.Companion.random(
    nickname: String = Faker().name().fullName(),
    email: String = Faker().internet().emailAddress()
) = CreateUserRequest(
    nickname = nickname,
    email = email
)

fun EditUserRequest.Companion.random(
    nickname: String? = Faker().name().fullName(),
    email: String? = Faker().internet().emailAddress()
) = EditUserRequest(
    nickname = nickname?.toOptional(),
    email = email?.toOptional()
)
