/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.event

import com.github.francescojo.core.domain.IdentifiableObject
import com.github.francescojo.core.domain.ValueHolder

/**
 * @since 2024-08-07
 */
interface DomainObjectEvent<T : IdentifiableObject<ID>, ID : ValueHolder<*>> {
    val source: Any

    val id: ID

    /**
     * The actual data that is being carried by this event. `null` means that the data is removed.
     */
    val data: T?
}
