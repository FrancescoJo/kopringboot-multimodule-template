/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.infra.repository

import com.github.francescojo.core.domain.IdentifiableObject
import com.github.francescojo.core.domain.ValueHolder
import com.github.francescojo.core.exception.internal.DataIntegrityBrokenException
import com.github.francescojo.core.repository.WritableRepository

/**
 * @since 2024-08-06
 */
abstract class AbstractWritableRepositoryTemplate<T : IdentifiableObject<ID>, ID : ValueHolder<*>> :
    WritableRepository<T, ID> {
    abstract fun findAllByIds(ids: Collection<ID>, lockRecords: Boolean): List<T>

    protected abstract fun createAll(models: Collection<T>): List<T>

    protected abstract fun updateAll(models: Collection<T>): List<T>

    override fun findAllByIds(ids: Collection<ID>): List<T> = findAllByIds(ids, lockRecords = false)

    /**
     * We should query models to determine whether the intention of this save is to create or update.
     * Therefore, the number of queries can be increased exponentially if there are large number of models are applied.
     * Consider applying a cache to individual [AbstractWritableRepositoryTemplate] implementations
     * if excessive queries are a problem.
     */
    override fun saveAll(models: Collection<T>): List<T> {
        val results = ArrayList<T>(models.size)

        // TODO: region Transaction required
        val comparisons = findAllByIds(models.map { it.id }, lockRecords = true).associateBy { it.id }

        // CREATE 와 UPDATE candidate 를 가려낸다.
        val (createCandidates, updateCandidates) = models.partition {
            !it.isIdentified || comparisons[it.id] == null
        }

        val updatedEntities = updateCandidates.takeIf { it.isNotEmpty() }?.run { updateAll(this) } ?: emptyList()
        val createdEntities = createCandidates.takeIf { it.isNotEmpty() }?.run { createAll(this) } ?: emptyList()

        results.addAll(updatedEntities)
        results.addAll(createdEntities)

        if (results.size != models.size) {
            throw DataIntegrityBrokenException("Failed to save all objects")
        }
        // endregion

        return results
    }
}
