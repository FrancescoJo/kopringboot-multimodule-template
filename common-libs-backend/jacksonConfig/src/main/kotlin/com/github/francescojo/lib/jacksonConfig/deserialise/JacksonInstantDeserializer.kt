/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.jacksonConfig.deserialise

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.github.francescojo.lib.time.truncateToSeconds
import java.time.Instant
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 * @since 2024-08-19
 */
internal class JacksonInstantDeserializer : JsonDeserializer<Instant>() {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): Instant? {
        return p?.let { ZonedDateTime.parse(it.text, DateTimeFormatter.ISO_DATE_TIME).toInstant().truncateToSeconds() }
    }
}
