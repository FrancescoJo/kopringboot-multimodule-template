/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.text

import com.vdurmont.emoji.EmojiParser
import java.text.BreakIterator
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

/**
 * Counts actual [Unicode Grapheme count](https://unicode.org/reports/tr29/#Grapheme_Cluster_Boundaries)
 * of given String. For example:
 *
 * | Category       | Literal                              | Length      | Grapheme count |
 * | :---           | :---------                           | :---        | :---           |
 * | Thai composite | บล็อกยูนิโคด                         | 12          | 9              |
 * | Emoji          | 🏴󠁧󠁢󠁷󠁬󠁳󠁿                              | 14          | 1              |
 * | Korean NFD     | \u1100\u1161\u1102\u1161\u1103\u1161 | 6           | 3              |
 *
 * @since 2022-02-14
 */
fun String.unicodeGraphemeCount(): Int {
    var count = 0
    EmojiIterator.iterateOn(this) { ++count }
    return count
}

/**
 * @since 2022-02-14
 */
private object EmojiIterator : EmojiParser() {
    /**
     * [List of invisible Unicode characters](http://unicode.org/faq/unsup_char.html)
     *
     * See also: https://www.fileformat.info/info/unicode/category/Cf/list.htm
     */
    @Suppress("MagicNumber")
    private val INVISIBLE_UNICODE_CHARS = setOf(
        0x00AD,                                                         // Soft hyphen
        0x115F, 0x1160,                                                 // Hangul Jamo fillers
        0x200B,                                                         // Zero-width space
        0x200C, 0x200D,                                                 // Cursive joiners
        0x200E, 0x200F, 0x202A, 0x202B, 0x202C, 0x202D, 0x202E,         // Bidirectional format controls
        0x205F, 0x2061, 0x2062, 0x2063, 0x2064, 0x2065, 0x2066, 0x2067, // General punctuations
        0x2068, 0x2069, 0x206A, 0x206B, 0x206C, 0x206D, 0x206E, 0x206F, // General punctuations (cont'd)
        0xFE00, 0xFE01, 0xFE02, 0xFE03, 0xFE04, 0xFE05, 0xFE06, 0xFE07, // Variation selectors
        0xFE08, 0xFE09, 0xFE0A, 0xFE0B, 0xFE0C, 0xFE0D, 0xFE0E, 0xFE0F, // Variation selectors (cont'd)
        0x2060, 0xFEFF                                                  // Word joiners
    )

    fun iterateOn(seq: String, callback: (String) -> Unit) {
        if (seq.isEmpty()) {
            return
        }

        val it = BreakIterator.getCharacterInstance().apply { setText(seq) }
        var idx = 0

        while (idx < seq.length) {
            val emojiEnd = getEmojiEndPos(seq.toCharArray(), idx)
            if (emojiEnd > -1) {
                callback(seq.substring(idx, emojiEnd))
                idx = emojiEnd
                continue
            }

            val codePoint = seq.codePointAt(idx)

            val oldIdx = idx
            idx = it.following(idx)

            if (!INVISIBLE_UNICODE_CHARS.contains(codePoint)) {
                callback(seq.substring(oldIdx, idx))
            }
        }
    }
}
