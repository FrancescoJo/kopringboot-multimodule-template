/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.webApi.response.base

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription

/**
 * @since 2021-08-10
 */
data class ErrorResponseEnvelope(override val body: Body) : ResponseEnvelope<ErrorResponseEnvelope.Body>(Type.ERROR) {
    data class Body(
        @get:JsonPropertyDescription(DESC_BODY_MESSAGE)
        @get:JsonProperty
        val message: String,

        @get:JsonPropertyDescription(DESC_BODY_CODE)
        @get:JsonProperty
        val code: String,

        @get:JsonPropertyDescription(DESC_BODY_DETAILS)
        @get:JsonProperty
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
