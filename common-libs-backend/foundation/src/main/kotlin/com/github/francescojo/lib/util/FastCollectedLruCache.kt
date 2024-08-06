/*
 * FJ's utilities
 *
 * Distributed under no licences and no warranty.
 * Use this software at your own risk.
 */
package com.github.francescojo.lib.util

import java.lang.ref.SoftReference
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.roundToInt

/**
 * A simple LRU cache implementation. This cache maybe useful for memory consuming objects.
 * Based on initial cache size, stored items which access rate drops lower than 25% will be
 * aggressively cleared from memory(It depends on GC settings - its implementation relies on
 * [java.lang.ref.SoftReference]).
 *
 * Due to its GC friendly feature, many operations that depends on size such as  `size()`,
 * `entrySet()`, etc., could derive inconsistent results, therefore no such map operations are
 * offered.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Nov - 2018
 */
interface FastCollectedLruCache<K : Any, V> {
    /**
     * @return the previous value associated with key, or `null` if there was no association.
     */
    fun put(key: K, value: V): V?

    fun get(key: K): V?

    fun remove(key: K): V?

    fun clear()

    companion object {
        fun <K : Any, V> create(maxCapacity: Int): FastCollectedLruCache<K, V> = FastCollectedLruCacheImpl(maxCapacity)
    }
}

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 23 - Nov - 2018
 */
internal class FastCollectedLruCacheImpl<K : Any, V>(maxCapacity: Int) : FastCollectedLruCache<K, V> {
    @Suppress("MagicNumber")
    private val hardCacheSize = (maxCapacity * 0.75f).roundToInt()
    private val softCacheSize = maxCapacity - hardCacheSize

    @Suppress("MagicNumber")
    internal val hardCache = object : LinkedHashMap<K, V>(hardCacheSize, 0.75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<K, V>): Boolean {
            if (size > hardCacheSize) {
                softCache[eldest.key] = SoftReference(eldest.value)
                return true
            }

            return false
        }
    }
    internal val softCache = ConcurrentHashMap<K, SoftReference<V>>(softCacheSize)

    override fun put(key: K, value: V): V? {
        softCache.remove(key)
        synchronized(hardCache) {
            return hardCache.put(key, value)
        }
    }

    override fun get(key: K): V? {
        synchronized(hardCache) {
            hardCache[key]?.let { v ->
                // Move element to first position of hard cache
                hardCache.remove(key)
                hardCache[key] = v
                return v
            }
        }

        var maybeValue: V? = null
        softCache[key]?.takeIf { it.get() != null }?.get()?.let { v ->
            synchronized(hardCache) {
                // Move element to first position of hard cache
                hardCache.remove(key)
                hardCache[key] = v
            }
            maybeValue = v
        }

        softCache.remove(key)
        return maybeValue
    }

    override fun remove(key: K): V? {
        softCache.remove(key)
        synchronized(hardCache) {
            return hardCache.remove(key)
        }
    }

    override fun clear() {
        synchronized(hardCache) {
            hardCache.clear()
        }
        softCache.clear()
    }
}
