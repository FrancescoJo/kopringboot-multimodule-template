/*
 * kopringboot-multimodule-template
 * Distributed under CC BY-NC-SA
 */
package com.github.francescojo.lib.util

import java.util.*

/**
 * A 'nil' UUID for emptiness checks. UUID value: "00000000-0000-0000-0000-000000000000"
 */
val EMPTY_UUID = UUID(0, 0)

private const val REGEX_HEXADECIMAL = "[0-9a-fA-F]"
/**
 * Matches all UUID patterns through version 1 to 5. Note that JVM [UUID] implementation uses UUID v4 by default.
 */
const val REGEX_UUID = "$REGEX_HEXADECIMAL{8}-" +
        "$REGEX_HEXADECIMAL{4}-" +
        "[1-5]$REGEX_HEXADECIMAL{3}-" +
        "[89AaBb]$REGEX_HEXADECIMAL{3}-" +
        "$REGEX_HEXADECIMAL{12}"
