/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.domain.user

import com.github.francescojo.core.domain.UuidValueHolder
import java.io.Serial
import java.io.Serializable
import java.util.*

/**
 * @since 2021-08-10
 */
@JvmInline value class UserId(override val value: UUID) : UuidValueHolder, Serializable {
    companion object {
        @Suppress("ConstPropertyName")  // JVM static field
        @Serial
        private const val serialVersionUID: Long = -1L
    }
}
