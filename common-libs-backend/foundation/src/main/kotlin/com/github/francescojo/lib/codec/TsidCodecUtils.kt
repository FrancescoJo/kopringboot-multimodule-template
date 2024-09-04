/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.codec

import io.hypersistence.tsid.TSID

/**
 * @since 2024-08-19
 */
object TsidCodecUtils {
    @JvmStatic
    fun tsidToString(value: TSID): String = value.format("%z")

    @JvmStatic
    fun stringToTsid(value: String): TSID = TSID.unformat(value, "%z")
}
