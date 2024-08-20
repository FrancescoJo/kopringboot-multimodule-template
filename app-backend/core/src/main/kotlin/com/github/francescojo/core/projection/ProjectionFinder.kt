/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.projection

import com.github.francescojo.core.domain.IdentifiableObject
import com.github.francescojo.core.domain.ValueHolder
import com.github.francescojo.lib.collection.assertSingleOrNull

/**
 * A base projection finder interface.
 * 'Projection' is defined and used as a read model in CQRS context.
 *
 * @since 2024-08-06
 */
interface ProjectionFinder<T : IdentifiableObject<ID>, ID : ValueHolder<*>> {
    fun getById(id: ID): T

    fun findById(id: ID): T? =
        findAllByIds(setOf(id)).assertSingleOrNull()

    fun findAllByIds(ids: Set<ID>): List<T>
}
