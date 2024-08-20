/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package testcase.small.text

import com.github.francescojo.lib.annotation.SmallTest
import com.github.francescojo.lib.text.isNullOrUnicodeBlank
import com.github.francescojo.lib.text.matchesIn
import com.github.francescojo.lib.text.unicodeGraphemeCount
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.regex.Pattern
import java.util.stream.Stream

/**
 * @since 2024-08-20
 */
@SmallTest
internal class StringUtilsSpec {
    @Test
    fun `isUnicodeBlank yields expected result upon given input`() {
        val withInput: (String) -> Boolean = { it.isNullOrUnicodeBlank() }

        assertAll(
            { withInput("") shouldBe true },
            { withInput("   ") shouldBe true },
            { withInput(" a ") shouldBe false },
            { withInput("\u3000 a \u3000") shouldBe false },
            { withInput("\u3000\u3000") shouldBe true }
        )
    }

    @ParameterizedTest(name = "\"{1}\" glyph count must be: {0}")
    @MethodSource("testUnicodeGraphemeCountArgs")
    fun `unicodeGraphemeCount for various inputs`(length: Int, charSeq: String) {
        length shouldBe charSeq.unicodeGraphemeCount()
    }

    @ParameterizedTest(name = "\"{0}\".matchesIn(\"{1}\") ==> ''{2}''")
    @MethodSource("matchesInTestArgsProvider")
    fun `matchesIn test for various inputs`(str: String, pattern: Pattern, expected: Boolean) {
        str.matchesIn(pattern) shouldBe expected
    }

    companion object {
        @Suppress("unused")
        @JvmStatic
        private fun matchesInTestArgsProvider(): Stream<Arguments> {
            return Stream.of(
                Arguments.of("1234567890", Pattern.compile("""\d+"""), true),
                Arguments.of("AMZamz", Pattern.compile("""[A-Z]{3}[a-z]{3}"""), true),
                Arguments.of("1992Apd", Pattern.compile("""\d+$"""), false)
            )
        }

        @JvmStatic
        @Suppress("unused")
        fun testUnicodeGraphemeCountArgs(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(0, ""),
                // Thai NFC case
                Arguments.of(28, "I 💚 Thai(บล็อกยูนิโคด) language"),
                // Emoji (7 characters for 1 glyph)
                Arguments.of(1, "🏴󠁧󠁢󠁷󠁬󠁳󠁿"),
                // Chinese
                Arguments.of(14, "威爾士國旗看起來像： 🏴󠁧󠁢󠁷󠁬󠁳󠁿🏴󠁧󠁢󠁷󠁬󠁳󠁿🏴󠁧󠁢󠁷󠁬󠁳󠁿"),
                // Japanese
                Arguments.of(10, "日本🇯🇵 大好き💚️!!"),
                // Korean NFD case
                Arguments.of(3, "\u1100\u1161\u1102\u1161\u1103\u1161")
            )
        }
    }
}
