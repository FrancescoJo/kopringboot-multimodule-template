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
    @get:Column(name = COL_IS_DELETED)
    var isDeleted: Boolean = false
) {
    override fun toString(): String = "${SoftDeletableEmbedded::class.simpleName}(${::isDeleted.name}=$isDeleted)"

    companion object {
        const val COL_IS_DELETED = "is_deleted"
    }
}
