/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.endpoint.v1.user.edit

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.github.francescojo.core.domain.user.User
import com.github.francescojo.core.domain.user.usecase.EditUserUseCase
import javax.annotation.Nullable
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Null
import javax.validation.constraints.Size

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
    override val nickname: String?,

    @field:Email
    @field:Nullable
    @field:Size(max = User.LENGTH_EMAIL_MAX)
    @JsonProperty
    @JsonPropertyDescription(DESC_EMAIL)
    override val email: String?,
) : EditUserUseCase.EditUserMessage {
    fun isEmpty() = nickname.isNullOrEmpty() && email.isNullOrEmpty()

    companion object {
        const val DESC_NAME = ""
        const val DESC_EMAIL = ""
    }
}
