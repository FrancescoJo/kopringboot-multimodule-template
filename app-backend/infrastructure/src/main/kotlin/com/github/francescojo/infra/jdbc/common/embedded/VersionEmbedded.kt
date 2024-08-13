/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.infra.jdbc.common.embedded

import com.github.francescojo.infra.jdbc.common.Versioned
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.Version

/**
 * Assumes that entity using this embeddable has a LONG type of version column.
 *
 * @since 2024-08-13
 */
@Embeddable
class VersionEmbedded(
    @get:Column(name = COL_VERSION)
    @get:Version
    override var version: Long = Versioned.DEFAULT_LONG_INT
) : Versioned<Long> {
    @Transient
    override val versionColumnName: String = COL_VERSION

    override fun increase(): Long = ++version

    companion object {
        const val COL_VERSION = "version"
    }
}
