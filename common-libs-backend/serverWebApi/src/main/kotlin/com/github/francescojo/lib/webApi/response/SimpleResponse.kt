/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.webApi.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.fasterxml.jackson.databind.annotation.JsonSerialize

/**
 * @since 2021-08-10
 */
@JsonSerialize
data class SimpleResponse<T>(
    @get:JsonProperty
    @get:JsonPropertyDescription(DESC_RESULT)
    val result: T
) {
    companion object {
        const val DESC_RESULT = ""
    }
}
