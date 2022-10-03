/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.endpoint.v1.user.common

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.github.francescojo.core.domain.user.User
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
    @JsonPropertyDescription(DESC_REGISTERED_AT)
    val registeredAt: Instant,

    @JsonProperty
    @JsonPropertyDescription(DESC_LAST_ACTIVE_AT)
    val lastActiveAt: Instant
) {
    companion object {
        const val DESC_ID = ""
        const val DESC_NICKNAME = ""
        const val DESC_EMAIL = ""
        const val DESC_REGISTERED_AT = ""
        const val DESC_LAST_ACTIVE_AT = ""

        fun from(src: User) = with(src) {
            UserResponse(
                id = id,
                nickname = nickname,
                email = email,
                registeredAt = registeredAt,
                lastActiveAt = lastActiveAt
            )
        }
    }
}
