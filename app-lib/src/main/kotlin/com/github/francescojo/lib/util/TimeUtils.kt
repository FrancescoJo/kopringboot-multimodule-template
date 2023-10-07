/*
 * kopringboot-multimodule-template
 * Distributed under CC BY-NC-SA
 */
package com.github.francescojo.lib.util

import java.time.Instant
import java.time.temporal.ChronoUnit

/**
 * Syntactic sugar for removing milliseconds and nanoseconds part of given Instant.
 *
 * @since 2022-02-14
 */
fun Instant.truncateToSeconds(): Instant =
    this.truncatedTo(ChronoUnit.SECONDS)
