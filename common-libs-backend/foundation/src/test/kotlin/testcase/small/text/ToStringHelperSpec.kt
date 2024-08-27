/*
 * kopringboot-multimodule-monorepo-template
 * Sir.LOIN Intellectual property. All rights reserved.
 */
package testcase.small.text

import com.github.francescojo.lib.annotation.SmallTest
import com.github.francescojo.lib.text.ToStringHelper.prettyToString
import com.github.francescojo.lib.text.ToStringStyle
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

/**
 * @since 2024-08-27
 */
@SmallTest
class ToStringHelperSpec {
    @DisplayName("JSON ToStringHelper should:")
    @Nested
    inner class JsonStringStyleShould {
        @DisplayName("convert any newline characters inside any strings to newline literal, to preserve JSON format")
        @Test
        fun multilineTransformed() {
            // then:
            val result = prettyToString(
                toStringTarget = this,
                attributes = mapOf("str" to "hello\nworld"),
                printSignature = false,
                style = ToStringStyle.json(multiline = false)
            )

            // expect:
            result shouldBe """{"str":"hello\nworld"}"""
        }

        @ParameterizedTest(name = "serialise {0} as single-lined JSON string")
        @MethodSource("testcase.small.text.ToStringHelperSpec#toJsonStringSingleLineTestArgs")
        fun toJsonStringSingleLine(
            @Suppress("UNUSED_PARAMETER") testName: String,
            toStringTarget: Map<String, Any>,
            expected: String
        ) {
            // then:
            val result = prettyToString(
                toStringTarget = this,
                attributes = toStringTarget,
                printSignature = false,
                style = ToStringStyle.json(multiline = false)
            )

            // expect:
            result shouldBe expected
        }

        @ParameterizedTest(name = "serialise {0} as multi-lined JSON string")
        @MethodSource("testcase.small.text.ToStringHelperSpec#toJsonStringMultiLineTestArgs")
        fun toJsonStringMultiLine(
            @Suppress("UNUSED_PARAMETER") testName: String,
            toStringTarget: Map<String, Any>,
            expected: String
        ) {
            // then:
            val result = prettyToString(
                toStringTarget = this,
                attributes = toStringTarget,
                printSignature = false,
                style = ToStringStyle.json(multiline = true)
            )

            // expect:
            result shouldBe expected
        }
    }

    companion object {
        @JvmStatic
        fun toJsonStringSingleLineTestArgs(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "simple object",
                mapOf("num" to 42, "str" to "hello"),
                """{"num":42,"str":"hello"}"""
            ),
            Arguments.of(
                "object which has nested map",
                mapOf("num" to 36, "map" to mapOf("key" to "value")),
                """{"num":36,"map":{"key":"value"}}"""
            ),
            Arguments.of(
                "object which has nested map inside collection",
                mapOf(
                    "num" to 42,
                    "maps" to mapOf(
                        "key1" to "val\n\nue1",
                        "maps2" to mapOf(
                            "key1" to "value1",
                            "arrays" to listOf(
                                1,
                                mapOf("a" to "b", "m" to mapOf("m1" to "m2", "m3" to "m4"), "c" to "d"),
                                3
                            )
                        ),
                        "key3" to "value3"
                    )
                ),
                """{"num":42,"maps":{"key1":"val\n\nue1","maps2":{"key1":"value1","arrays":[1, {a=b, m={m1=m2, m3=m4}, c=d}, 3]},"key3":"value3"}}"""
            ),
        )

        @JvmStatic
        fun toJsonStringMultiLineTestArgs(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "simple object",
                mapOf("num" to 42),
                """
                    {
                      "num": 42
                    }
                """.trimIndent()
            ),
            Arguments.of(
                "simple object with multiple values",
                mapOf("num" to 42, "str" to "hello"),
                """
                    {
                      "num": 42,
                      "str": "hello"
                    }
                """.trimIndent()
            ),
            Arguments.of(
                "object which has nested map",
                mapOf("num" to 36, "map" to mapOf("key" to "value")),
                """
                    {
                      "num": 36,
                      "map": {
                        "key": "value"
                      }
                    }
                """.trimIndent()
            ),
            Arguments.of(
                "object which has nested map inside collection",
                mapOf(
                    "num" to 42,
                    "maps" to mapOf(
                        "key1" to "val\n\nue1",
                        "maps2" to mapOf(
                            "key1" to "value1",
                            "arrays" to listOf(
                                1,
                                mapOf("a" to "b", "m" to mapOf("m1" to "m2", "m3" to "m4"), "c" to "d"),
                                3
                            )
                        ),
                        "key3" to "value3"
                    )
                ),
                """
                    {
                      "num": 42,
                      "maps": {
                        "key1": "val\n\nue1",
                        "maps2": {
                          "key1": "value1",
                          "arrays": [
                            1,
                            {
                              "a": "b",
                              "m": {
                                "m1": "m2",
                                "m3": "m4"
                              },
                              "c": "d"
                            },
                            3
                          ]
                        },
                        "key3": "value3"
                      }
                    }
                """.trimIndent()
            ),
        )
    }
}
