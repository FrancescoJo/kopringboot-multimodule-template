/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.jacksonConfig.serialise

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.github.francescojo.lib.codec.TsidCodecUtils.tsidToString
import io.hypersistence.tsid.TSID

/**
 * @since 2024-08-19
 */
internal class JacksonTsidSerializer : JsonSerializer<TSID>() {
    override fun serialize(value: TSID?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        gen?.writeString(value?.let { tsidToString(it) })
    }
}
