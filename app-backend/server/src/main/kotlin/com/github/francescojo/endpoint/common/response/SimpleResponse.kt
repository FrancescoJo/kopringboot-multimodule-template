/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.endpoint.common.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.fasterxml.jackson.databind.annotation.JsonSerialize

/**
 * @since 2021-08-10
 */
@JsonSerialize
data class SimpleResponse<T>(
    @JsonProperty
    @JsonPropertyDescription(DESC_RESULT)
    val result: T
) {
    companion object {
        const val DESC_RESULT = ""
    }
}
