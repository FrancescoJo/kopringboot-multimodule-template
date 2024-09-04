/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.springWebMvc.converter

import com.github.francescojo.lib.codec.TsidCodecUtils.stringToTsid
import io.hypersistence.tsid.TSID
import org.springframework.core.convert.converter.Converter

/**
 * @since 2024-08-13
 */
internal class StringToTsidConverter : Converter<String, TSID> {
    override fun convert(source: String): TSID = stringToTsid(source)
}
