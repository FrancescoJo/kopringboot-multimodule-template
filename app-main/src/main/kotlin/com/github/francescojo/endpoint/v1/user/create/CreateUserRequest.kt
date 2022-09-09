/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.endpoint.v1.user.create

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.github.francescojo.core.domain.user.User
import com.github.francescojo.core.domain.user.usecase.CreateUserUseCase
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

/**
 * @since 2021-08-10
 */
@JsonDeserialize
data class CreateUserRequest(
    @field:NotEmpty
    @field:Size(min = User.LENGTH_NAME_MIN, max = User.LENGTH_NAME_MAX)
    @JsonProperty
    @JsonPropertyDescription(DESC_NAME)
    override val nickname: String,

    @field:NotEmpty
    @field:Email
    @field:Size(max = User.LENGTH_EMAIL_MAX)
    @JsonProperty
    @JsonPropertyDescription(DESC_EMAIL)
    override val email: String,
): CreateUserUseCase.CreateUserMessage {
    companion object {
        const val DESC_NAME = ""
        const val DESC_EMAIL = ""
    }
}
