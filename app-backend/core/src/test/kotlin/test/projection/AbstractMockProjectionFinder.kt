/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package test.projection

import com.github.francescojo.core.domain.IdentifiableObject
import com.github.francescojo.core.domain.ValueHolder
import com.github.francescojo.core.projection.ProjectionFinder
import test.repository.AbstractMockWritableRepository

/**
 * A simple mock projection finder. Use this class in SmallTest if mocking various [ProjectionFinder]
 * instances became cumbersome.
 *
 * Unlike [AbstractMockWritableRepository], this implementation does not provide any functionalities that
 * depend on the behaviour of persistent unit - since the main concern of [ProjectionFinder] is specialised on reading.
 * Therefore, you should write test scenarios with an assumption that the ID of your Projection
 * has already been determined.
 *
 * @since 2024-08-07
 */
abstract class AbstractMockProjectionFinder<T : IdentifiableObject<ID>, ID : ValueHolder<*>> :
    ProjectionFinder<T, ID> {
    private val idStore = LinkedHashMap<ID, T>()

    // region Test utility Methods
    fun clearMocks() {
        idStore.clear()
        onClear()
    }

    protected open fun onSave(model: T): T = model

    protected open fun onClear() {}

    protected fun store() = idStore

    fun save(projection: T): T? = idStore.put(projection.id, projection).also { onSave(projection) }

    fun deleteById(id: ID): T? = idStore.remove(id)
    // endregion

    // region ProjectionFinder Methods
    override fun findAllByIds(ids: Set<ID>): List<T> = ids.mapNotNull { idStore[it] }
    // endregion
}
