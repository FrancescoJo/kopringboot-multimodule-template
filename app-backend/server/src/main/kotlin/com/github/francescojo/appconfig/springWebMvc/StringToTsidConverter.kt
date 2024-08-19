/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.appconfig.springWebMvc

import com.github.francescojo.lib.codec.TsidCodecMixin
import io.hypersistence.tsid.TSID
import org.springframework.core.convert.converter.Converter

/**
 * @since 2024-08-13
 */
class StringToTsidConverter : Converter<String, TSID>, TsidCodecMixin {
    override fun convert(source: String): TSID = source.deserialiseToTsid()
}
