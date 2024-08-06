/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.endpoint.v1.user.common

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.github.francescojo.core.domain.user.projection.UserProjection
import java.time.Instant
import java.util.*

/**
 * @since 2021-08-10
 */
@JsonSerialize
data class UserResponse(
    @JsonProperty
    @JsonPropertyDescription(DESC_ID)
    val id: UUID,

    @JsonProperty
    @JsonPropertyDescription(DESC_NICKNAME)
    val nickname: String,

    @JsonProperty
    @JsonPropertyDescription(DESC_EMAIL)
    val email: String,

    @JsonProperty
    @JsonPropertyDescription(DESC_CREATED_AT)
    val createdAt: Instant,

    @JsonProperty
    @JsonPropertyDescription(DESC_UPDATED_AT)
    val updatedAt: Instant
) {
    companion object {
        const val DESC_ID = ""
        const val DESC_NICKNAME = ""
        const val DESC_EMAIL = ""
        const val DESC_CREATED_AT = ""
        const val DESC_UPDATED_AT = ""

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
