/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.util

import java.math.BigDecimal

/**
 * Determines that given BigDecimal has only trailing zeros in fractions - means that it is an integer.
 */
fun BigDecimal.isIntegerValue() = signum() == 0 || scale() <= 0 || stripTrailingZeros().scale() <= 0

/**
 * Removes all ineffective numbers of BigDecimal if the receiver object is determined as an integer.
 */
fun BigDecimal.dropFractionalPartIfInteger(): BigDecimal = if (this.isIntegerValue()) {
    this.stripTrailingZeros()
} else {
    this
}
