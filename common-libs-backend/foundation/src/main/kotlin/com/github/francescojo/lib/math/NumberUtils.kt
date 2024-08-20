/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.math

import java.math.BigDecimal

/**
 * Determines that given BigDecimal has only trailing zeros in fractions - means that it is an integer.
 */
fun BigDecimal.isIntegerValue() = signum() == 0 || scale() <= 0 || stripTrailingZeros().scale() <= 0
