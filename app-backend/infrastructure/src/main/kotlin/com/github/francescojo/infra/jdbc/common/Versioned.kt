/*
 * kopringboot-multimodule-monorepo-template
 * Sir.LOIN Intellectual property. All rights reserved.
 */
package com.github.francescojo.infra.jdbc.common

/**
 * @since 2022-02-14
 */
interface Versioned<T : Comparable<T>> {
    var version: T

    val versionColumnName: String

    fun increase(): T

    companion object {
        const val DEFAULT_INT = 0
        const val DEFAULT_LONG_INT = 0L
    }
}
