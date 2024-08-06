/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.util

/**
 * Returns a view of the portion of this list between the specified [startIndex] and continues to the end of the list.
 * The returned list is shallow-copied of receiver List, so non-structural changes in the returned list
 * are reflected in this list, and vice-versa.
 *
 * Structural changes in the base list make the behaviour of the view undefined.
 */
fun <T> List<T>.subList(startIndex: Int): List<T> = subList(startIndex, this.size)

/**
 * The [singleOrNull] function of Kotlin standard library returns null if given receiver Collection is not single.
 * This leniency is not suitable for some cases - for example, when users want to get a single element or `null`,
 * but does not expect to get multiple elements.
 *
 * This function is a strict version of [singleOrNull] that throws [IllegalArgumentException]
 * if given receiver Collection is not empty or single.
 */
fun <T> Collection<T>.assertSingleOrNull(): T? = when (size) {
    0 -> null
    1 -> this.first()
    else -> throw IllegalArgumentException("Collection size is $size, but expected 0 or 1.")
}

/**
 * Inspired by [sampleSize](https://github.com/lodash/lodash/blob/main/src/sampleSize.ts) of lodash.
 * Syntactic sugar for [Iterable.shuffled] and [Iterable.take].
 */
fun <T> Iterable<T>.sample(size: Int = 1): List<T> = this.shuffled().take(size)
