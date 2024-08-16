/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.util

import java.util.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
fun Optional<*>?.isUndefined(): Boolean {
    contract {
        returns(false) implies (this@isUndefined != null)
    }

    return this == null
}

fun Optional<*>?.isUndefinedOrNull(): Boolean {
    return this == null || this.isEmpty
}

fun <T : Any> Optional<T>?.takeOr(default: () -> T): T = if (this.isUndefined()) {
    default.invoke()
} else {
    if (this.isEmpty) {
        default.invoke()
    } else {
        this.get()
    }
}

fun <T> Optional<T>?.takeOrNullable(default: () -> T?): T? = if (this.isUndefined()) {
    default.invoke()
} else {
    if (this.isEmpty) {
        default.invoke()
    } else {
        this.get()
    }
}

fun <T : Any> T?.toOptional(): Optional<T> = if (this == null) {
    Optional.empty()
} else {
    Optional.of(this)
}
