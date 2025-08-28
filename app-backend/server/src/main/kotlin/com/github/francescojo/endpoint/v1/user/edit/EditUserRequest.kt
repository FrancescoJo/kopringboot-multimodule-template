/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.endpoint.v1.user.edit

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.github.francescojo.core.domain.user.model.User
import com.github.francescojo.core.domain.user.usecase.EditUserUseCase
import com.github.francescojo.core.exception.external.WrongInputException
import com.github.francescojo.lib.util.isUndefinedOrNull
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.annotation.Nullable
import jakarta.validation.ConstraintViolationException
import jakarta.validation.Validator
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size
import java.util.*
import kotlin.jvm.optionals.getOrNull

/**
 * @since 2021-08-10
 */
@Schema(name = "v1.user.EditUserRequest")
@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_NULL)
data class EditUserRequest(
    @get:JsonProperty
    @get:JsonPropertyDescription(DESC_NICKNAME)
    val nickname: Optional<String>,

    @get:JsonProperty
    @get:JsonPropertyDescription(DESC_EMAIL)
    val email: Optional< String>,
) {
    fun toMessage(validator: Validator): EditUserUseCase.EditUserMessage {
        val isEmpty = nickname.isUndefinedOrNull() && email.isUndefinedOrNull()

        if (isEmpty) {
            throw WrongInputException(value = null, message = "Message is empty.")
        }

        with(validator.validate<Any>(ValidationTarget.from(this))) {
            if (isNotEmpty()) {
                throw ConstraintViolationException(this)
            }
        }

        return EditUserUseCase.EditUserMessage(
            nickname = nickname,
            email = email
        )
    }

    private data class ValidationTarget(
        @field:Nullable
        @field:Size(min = User.LENGTH_NAME_MIN, max = User.LENGTH_NAME_MAX)
        val nickname: String?,

        @field:Nullable
        @field:Email
        @field:Size(max = User.LENGTH_EMAIL_MAX)
        val email: String?,
    ) {
        companion object {
            fun from(request: EditUserRequest): ValidationTarget {
                return ValidationTarget(
                    nickname = request.nickname.getOrNull(),
                    email = request.email.getOrNull()
                )
            }
        }
    }

    companion object {
        const val DESC_NICKNAME = "Nickname of the user. Must not be empty and must be between 2 and 64 characters."
        const val DESC_EMAIL = "Email of the user. Must not be empty, must be a valid email format, and between 5 and 64 characters."
    }
}
