/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.endpoint

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.fasterxml.jackson.annotation.JsonValue
import java.time.Instant

/**
 * @since 2021-08-10
 */
abstract class ResponseEnvelope<T>(
    @JsonPropertyDescription(DESC_TYPE)
    @JsonProperty
    open val type: Type,

    @JsonPropertyDescription(DESC_TIMESTAMP)
    @JsonProperty
    open val timestamp: Instant = Instant.now()
) {
    @get:JsonPropertyDescription(DESC_BODY)
    @get:JsonProperty
    abstract val body: T?

    companion object {
        const val DESC_TYPE = "Indicates response type. either 'OK' or 'ERROR'."
        const val DESC_TIMESTAMP = "Response time in UTC that formatted in ISO-8601 Standard."
        const val DESC_BODY = "Actual response body."

        fun <T> ok(payload: T?) = OkResponseEnvelope(payload)

        fun error(message: String, code: String, details: Any?) = ErrorResponseEnvelope.create(
            message, code, details
        )
    }

    enum class Type(@JsonValue val value: String) {
        OK("OK"),
        ERROR("ERROR");

        companion object {
            @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
            @JvmStatic
            fun from(value: String?) = values().firstOrNull { it.value == value }
                ?: throw IllegalArgumentException("Cannot convert '${value}' as Response type")
        }
    }
}
