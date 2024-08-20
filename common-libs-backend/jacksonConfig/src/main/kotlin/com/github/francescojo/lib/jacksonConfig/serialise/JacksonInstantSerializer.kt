/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.jacksonConfig.serialise

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.github.francescojo.lib.time.truncateToSeconds
import java.time.Instant
import java.time.ZoneOffset.UTC
import java.time.format.DateTimeFormatter

/**
 * @since 2024-08-19
 */
internal class JacksonInstantSerializer : JsonSerializer<Instant>() {
    override fun serialize(value: Instant?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        gen?.writeString(value?.truncateToSeconds()?.atZone(UTC)?.format(DateTimeFormatter.ISO_DATE_TIME))
    }
}
