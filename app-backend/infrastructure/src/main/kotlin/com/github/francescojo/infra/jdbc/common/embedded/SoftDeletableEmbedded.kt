/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.infra.jdbc.common.embedded

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

/**
 * @since 2022-08-19
 */
@Embeddable
class SoftDeletableEmbedded(
    @get:Column(name = COLUMN_DELETED)
    var deleted: Boolean = false
) {
    companion object {
        const val COLUMN_DELETED = "deleted"
    }
}
