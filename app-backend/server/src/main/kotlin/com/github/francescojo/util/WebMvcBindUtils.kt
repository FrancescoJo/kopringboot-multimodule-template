/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.util

import io.hypersistence.tsid.TSID

/**
 * @since 2024-08-13
 */
interface WebMvcBindUtils {
    fun TSID.serialise(): String = format("%z")

    fun String.deserialiseToTsid(): TSID = TSID.unformat(this, "%z")
}
