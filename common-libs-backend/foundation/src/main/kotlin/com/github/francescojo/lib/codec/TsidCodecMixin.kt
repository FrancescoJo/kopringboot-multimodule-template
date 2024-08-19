/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.codec

import io.hypersistence.tsid.TSID

/**
 * @since 2024-08-19
 */
interface TsidCodecMixin {
    fun TSID.serialise(): String = format("%z")

    fun String.deserialiseToTsid(): TSID = TSID.unformat(this, "%z")
}
