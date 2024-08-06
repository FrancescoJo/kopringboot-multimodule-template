/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.repository

import com.github.francescojo.core.domain.IdentifiableObject
import com.github.francescojo.core.domain.ValueHolder

/**
 * @since 2024-08-06
 */
interface WritableRepository<T : IdentifiableObject<ID>, ID : ValueHolder<*>> : ReadonlyRepository<T, ID> {
    fun save(model: T): T =
        saveAll(setOf(model)).single()

    fun saveAll(models: Collection<T>): List<T>

    fun delete(model: T): Boolean =
        deleteById(model.id)

    fun deleteById(id: ID): Boolean =
        deleteAllByIds(setOf(id)) == 1L

    fun deleteAllByIds(ids: Collection<ID>): Long
}
