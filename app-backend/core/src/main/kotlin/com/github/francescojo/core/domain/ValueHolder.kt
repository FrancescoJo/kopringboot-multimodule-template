/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.domain

import com.github.francescojo.lib.util.EMPTY_UUID
import io.hypersistence.tsid.TSID
import java.util.*

/**
 * A type placeholder to represent various `value class`es as a single type.
 *
 * @since 2023-11-15
 */
interface ValueHolder<T : Any> {
    val value: T

    val isEmpty: Boolean
}

interface NumberValueHolder<T : Number> : ValueHolder<T> {
    override val isEmpty: Boolean
        get() = value.toLong() == EMPTY_VALUE_LONG

    companion object {
        const val EMPTY_VALUE_INT: Int = 0

        const val EMPTY_VALUE_LONG: Long = 0L
    }
}

interface StringValueHolder : ValueHolder<String> {
    override val isEmpty: Boolean
        get() = value.isEmpty()

    companion object {
        const val EMPTY_VALUE: String = ""
    }
}

interface UuidValueHolder : ValueHolder<UUID> {
    override val isEmpty: Boolean
        get() = value == EMPTY_VALUE

    companion object {
        val EMPTY_VALUE: UUID = EMPTY_UUID
    }
}

interface TsidValueHolder : ValueHolder<TSID> {
    override val isEmpty: Boolean
        get() = value == EMPTY_VALUE

    companion object {
        val EMPTY_VALUE: TSID = TSID.from(0L)
    }
}
