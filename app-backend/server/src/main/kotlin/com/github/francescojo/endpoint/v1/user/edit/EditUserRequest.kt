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
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size
import java.util.*

/**
 * @since 2021-08-10
 */
@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_NULL)
data class EditUserRequest(
    @field:Nullable
    @field:Size(min = User.LENGTH_NAME_MIN, max = User.LENGTH_NAME_MAX)
    @JsonProperty
    @JsonPropertyDescription(DESC_NAME)
    val nickname: Optional<String>?,

    @field:Email
    @field:Nullable
    @field:Size(max = User.LENGTH_EMAIL_MAX)
    @JsonProperty
    @JsonPropertyDescription(DESC_EMAIL)
    val email: Optional<String>?,
) {
    fun toMessage(): EditUserUseCase.EditUserMessage {
        val isEmpty = nickname.isUndefinedOrNull() && email.isUndefinedOrNull()

        if (isEmpty) {
            throw WrongInputException("Message is empty.")
        }

        return EditUserUseCase.EditUserMessage(
            nickname = nickname,
            email = email
        )
    }

    companion object {
        const val DESC_NAME = ""
        const val DESC_EMAIL = ""
    }
}
