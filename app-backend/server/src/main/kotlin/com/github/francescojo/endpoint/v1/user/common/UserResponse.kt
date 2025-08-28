/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.endpoint.v1.user.common

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.github.francescojo.core.domain.user.projection.UserProjection
import io.hypersistence.tsid.TSID
import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant

/**
 * @since 2021-08-10
 */
@Schema(name = "v1.user.UserResponse")
@JsonSerialize
data class UserResponse(
    @get:JsonProperty
    @get:JsonPropertyDescription(DESC_ID)
    val id: TSID,

    @get:JsonProperty
    @get:JsonPropertyDescription(DESC_NICKNAME)
    val nickname: String,

    @get:JsonProperty
    @get:JsonPropertyDescription(DESC_EMAIL)
    val email: String,

    @get:JsonProperty
    @get:JsonPropertyDescription(DESC_CREATED_AT)
    val createdAt: Instant,

    @get:JsonProperty
    @get:JsonPropertyDescription(DESC_UPDATED_AT)
    val updatedAt: Instant
) {
    companion object {
        const val DESC_ID = "Unique identifier of the user."
        const val DESC_NICKNAME = "Nickname of the user."
        const val DESC_EMAIL = "Email of the user."
        const val DESC_CREATED_AT = "Timestamp when the user was created. Format: ISO 8601."
        const val DESC_UPDATED_AT = "Timestamp when the user information is modified. Format: ISO 8601."

        fun from(src: UserProjection) = with(src) {
            UserResponse(
                id = id.value,
                nickname = nickname,
                email = email,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }
    }
}
