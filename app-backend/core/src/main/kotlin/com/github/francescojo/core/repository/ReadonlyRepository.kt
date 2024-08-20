/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.repository

import com.github.francescojo.core.domain.IdentifiableObject
import com.github.francescojo.core.domain.ValueHolder
import com.github.francescojo.lib.collection.assertSingleOrNull

/**
 * @since 2024-08-06
 */
interface ReadonlyRepository<T : IdentifiableObject<ID>, ID : ValueHolder<*>> {
    fun getById(id: ID): T

    fun existsById(id: ID): Boolean =
        findById(id) != null

    fun findById(id: ID): T? =
        findAllByIds(setOf(id)).assertSingleOrNull()

    fun findAllByIds(ids: Collection<ID>): List<T>
}
