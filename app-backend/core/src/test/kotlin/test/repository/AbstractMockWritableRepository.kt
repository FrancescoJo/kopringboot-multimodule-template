/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package test.repository

import com.github.francescojo.core.domain.IdentifiableObject
import com.github.francescojo.core.domain.ValueHolder
import com.github.francescojo.core.repository.WritableRepository
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KClassifier
import kotlin.reflect.KProperty1
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

/**
 * A mock repository that supports ID generation. Use this class in SmallTest if mocking various [WritableRepository]
 * instances became cumbersome.
 *
 * @since 2024-08-06
 */
abstract class AbstractMockWritableRepository<T : IdentifiableObject<ID>, ID : ValueHolder<*>> :
    WritableRepository<T, ID> {
    private val idSyncLock = Any()
    private var autoSequence = 0L

    private val idStore = LinkedHashMap<ID, T>()
    private val onSaveListeners = LinkedHashSet<(T) -> Unit>()
    private val onDeleteListeners = LinkedHashSet<(ID) -> Unit>()
    private val onClearListeners = LinkedHashSet<() -> Unit>()

    // region Test utility Methods
    fun clearMocks() {
        synchronized(idSyncLock) {
            autoSequence = 0L
            idStore.clear()
        }

        onSaveListeners.clear()
        onDeleteListeners.clear()
        onClearListeners.forEach { it() }
        onClear()
    }

    protected open fun onSave(model: T): T {
        onSaveListeners.forEach { it(model) }

        return model
    }

    protected open fun onClear() = onClearListeners.forEach { it() }

    protected fun store() = idStore

    protected fun autoIdOf(model: T): ID {
        val newId = autoIdOf(findIdFieldOf(model))

        return newId ?: failByNoAutoIdPolicy(model.id::class.toString())
    }

    fun onSave(listener: (T) -> Unit) = onSaveListeners.add(listener)

    fun onDelete(listener: (ID) -> Unit) = onDeleteListeners.add(listener)

    fun onClear(listener: () -> Unit) = onClearListeners.add(listener)
    // endregion

    // region Repository Methods
    override fun findAllByIds(ids: Collection<ID>): List<T> = ids.mapNotNull { idStore[it] }

    override fun saveAll(models: Collection<T>): List<T> {
        val savedModels = ArrayList<T>(models.size)

        models.forEach { m ->
            if (m.id.isEmpty) {
                val idField = findIdFieldOf(m)

                val newId = synchronized(idSyncLock) {
                    val maybeId = autoIdOf(idField)

                    return@synchronized maybeId ?: failByNoAutoIdPolicy(idField.toString())
                }

                /*
                 * JVM reflection hack: we disable accessible check here,
                 * since the actual id implementation is public or private
                 */
                val idFieldJvm = m::class.java.getDeclaredField((IdentifiableObject<*>::id).name)
                idFieldJvm.isAccessible = true
                // This causes problem on inline classes; however, there are no suitable solutions for Jun 2024 now.
                idFieldJvm.set(m, newId)
                idFieldJvm.isAccessible = false
            }

            savedModels.add(m).also { idStore[m.id] = m }
        }

        return savedModels.onEach { onSave(it) }
    }

    override fun deleteAllByIds(ids: Collection<ID>): Long {
        var deletedCount = 0L

        ids.forEach { id ->
            val removed = idStore.remove(id)
            if (removed != null) {
                ++deletedCount

                onDeleteListeners.forEach { it(id) }
            }
        }

        return deletedCount
    }
    // endregion

    private fun failByNoAutoIdPolicy(type: String): Nothing =
        throw UnsupportedOperationException("There is no automatic value generation policy for type - $type")

    private fun findIdFieldOf(model: T): KProperty1<out T, *> {
        return model::class.memberProperties.find { it.name == (IdentifiableObject<*>::id).name }!!
    }

    @Suppress("UNCHECKED_CAST")     // Using reflection causes some unchecked casts.
    private fun autoIdOf(idField: KProperty1<out T, *>): ID? {
        val idFieldType = idField.returnType.classifier

        return if (idFieldType is KClass<*> && idFieldType.isSubclassOf(ValueHolder::class)) {
            val rawValueField =
                (idFieldType as KClass<ValueHolder<*>>).memberProperties.find { it.name == (ValueHolder<*>::value).name }!!

            val rawIdValue = rawIdValueOf(rawValueField.returnType.classifier)

            /* return if */ if (rawIdValue == null) {
                /* return if */ null
            } else {
                val constructor = idFieldType.primaryConstructor ?: throw UnsupportedOperationException(
                    "There is/are no mean(s) to inject id value upon $idField instance generation."
                )

                /* return if */ constructor.call(rawIdValue)
            }
        } else {
            /* return if */ rawIdValueOf(idFieldType)
        } as? ID
    }

    private fun rawIdValueOf(idType: KClassifier?): Any? {
        return when (idType) {
            Long::class -> ++autoSequence
            Int::class -> (++autoSequence).toInt()
            UUID::class -> UUID.randomUUID()
            else -> null
        }
    }
}
