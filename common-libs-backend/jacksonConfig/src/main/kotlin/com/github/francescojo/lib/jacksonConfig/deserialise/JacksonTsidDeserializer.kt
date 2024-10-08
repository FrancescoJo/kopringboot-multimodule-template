/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.jacksonConfig.deserialise

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.github.francescojo.lib.codec.TsidCodecUtils.stringToTsid
import io.hypersistence.tsid.TSID

/**
 * @since 2024-08-19
 */
internal class JacksonTsidDeserializer : JsonDeserializer<TSID>() {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): TSID? {
        return p?.text?.let { stringToTsid(it) }
    }
}
