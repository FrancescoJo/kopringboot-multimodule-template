/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.domain

import java.time.Instant

/**
 * @since 2022-08-19
 */
interface DateAuditable {
    val createdAt: Instant

    val updatedAt: Instant

    interface Mutator : DateAuditable {
        override var createdAt: Instant

        override var updatedAt: Instant
    }
}
