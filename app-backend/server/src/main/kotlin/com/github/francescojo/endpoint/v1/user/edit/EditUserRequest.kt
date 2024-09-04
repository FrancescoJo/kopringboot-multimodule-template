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
@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_NULL)
data class EditUserRequest(
    @JsonProperty
    @JsonPropertyDescription(DESC_NAME)
    val nickname: Optional<String>,

    @JsonProperty
    @JsonPropertyDescription(DESC_EMAIL)
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
        const val DESC_NAME = ""
        const val DESC_EMAIL = ""
    }
}
