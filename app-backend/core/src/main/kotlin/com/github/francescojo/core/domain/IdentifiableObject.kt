/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.domain

/**
 * @since 2022-08-19
 */
interface IdentifiableObject<T : ValueHolder<out Any>> {
    val id: T

    val isIdentified: Boolean
        get() = !id.isEmpty
}
