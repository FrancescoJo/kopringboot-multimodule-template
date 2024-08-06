/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.endpoint

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription

/**
 * @since 2021-08-10
 */
data class ErrorResponseEnvelope(override val body: Body) : ResponseEnvelope<ErrorResponseEnvelope.Body>(Type.ERROR) {
    data class Body(
        @JsonPropertyDescription(DESC_BODY_MESSAGE)
        @JsonProperty
        val message: String,

        @JsonPropertyDescription(DESC_BODY_CODE)
        @JsonProperty
        val code: String,

        @JsonPropertyDescription(DESC_BODY_DETAILS)
        @JsonProperty
        val details: Any? = null
    )

    companion object {
        const val DESC_BODY_MESSAGE = ""
        const val DESC_BODY_CODE = ""
        const val DESC_BODY_DETAILS = "Additional information to handle this error situation."

        fun create(message: String, code: String, details: Any?) =
            ErrorResponseEnvelope(Body(message, code, details))
    }
}
