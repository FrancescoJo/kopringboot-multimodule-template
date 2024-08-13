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
    @get:Column(name = COL_DELETED)
    var deleted: Boolean = false
) {
    override fun toString(): String = "${SoftDeletableEmbedded::class.simpleName}(${::deleted.name}=$deleted)"

    companion object {
        const val COL_DELETED = "deleted"
    }
}
