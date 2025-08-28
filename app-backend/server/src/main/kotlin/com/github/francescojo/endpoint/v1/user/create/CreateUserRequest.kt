/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.endpoint.v1.user.create

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.github.francescojo.core.domain.user.model.User
import com.github.francescojo.core.domain.user.usecase.CreateUserUseCase
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

/**
 * @since 2021-08-10
 */
@Schema(name = "v1.user.CreateUserRequest")
@JsonDeserialize
data class CreateUserRequest(
    @field:NotEmpty
    @field:Size(min = User.LENGTH_NAME_MIN, max = User.LENGTH_NAME_MAX)
    @get:JsonProperty
    @get:JsonPropertyDescription(DESC_NICKNAME)
    override val nickname: String,

    @field:NotEmpty
    @field:Email
    @field:Size(max = User.LENGTH_EMAIL_MAX)
    @get:JsonProperty
    @get:JsonPropertyDescription(DESC_EMAIL)
    override val email: String,
): CreateUserUseCase.CreateUserMessage {
    companion object {
        const val DESC_NICKNAME = "Nickname of the user. Must not be empty and must be between 2 and 64 characters."
        const val DESC_EMAIL = "Email of the user. Must not be empty, must be a valid email format, and between 5 and 64 characters."
    }
}
