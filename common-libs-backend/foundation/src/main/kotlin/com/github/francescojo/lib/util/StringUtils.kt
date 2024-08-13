/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.util

import java.util.*
import java.util.regex.Pattern

/*
 * Referenced from: https://www.unicode.org/Public/UCD/latest/ucd/PropList.txt
 * Pretty display at: https://www.fileformat.info/info/unicode/category/Zs/list.htm
 *
 * Type notation to ensure this collection is immutable even though a reference leakage happens
 */
private val UNICODE_BLANK_CHARS: Set<Char> = setOf(
    '\u0009', // control-0009
    '\u000A', // control-000A
    '\u000B', // control-000B
    '\u000C', // control-000C
    '\u000D', // control-000D
    '\u001C', // FILE SEPARATOR
    '\u001D', // GROUP SEPARATOR
    '\u001E', // RECORD SEPARATOR
    '\u001F', // UNIT SEPARATOR
    '\u0020', // SPACE
    '\u0085', // control-0085
    '\u00A0', // NO-BREAK SPACE
    '\u1680', // OGHAM SPACE MARK
    '\u2000', // EN QUAD
    '\u2001', // EM QUAD
    '\u2002', // EN SPACE
    '\u2003', // EM SPACE
    '\u2004', // THREE-PER-EM SPACE
    '\u2005', // FOUR-PER-EM SPACE
    '\u2006', // SIX-PER-EM SPACE
    '\u2007', // FIGURE SPACE
    '\u2008', // PUNCTUATION SPACE
    '\u2009', // THIN SPACE
    '\u200A', // HAIR SPACE
    '\u202F', // NARROW NO-BREAK SPACE
    '\u205F', // MEDIUM MATHEMATICAL SPACE
    '\u3000'  // IDEOGRAPHIC SPACE
)

/**
 * Determines given String is filled only by Unicode invisible letters.
 *
 * @since 2022-02-14
 */
@Suppress("ReturnCount")
fun String?.isNullOrUnicodeBlank(): Boolean {
    this?.forEach {
        if (!UNICODE_BLANK_CHARS.contains(it)) {
            return false
        }
    }

    return true
}

/**
 * Syntactic sugar for testing a String matches in given Regular Expression Pattern.
 *
 * @since 2022-02-14
 */
fun String.matchesIn(pattern: Pattern) = pattern.matcher(this).matches()

/**
 * Converts given byte array to hex-encoded string.
 *
 * Requires JDK 17.
 *
 * @since 2022-04-01
 */
fun ByteArray.toHexString() = HexFormat.of().formatHex(this)
