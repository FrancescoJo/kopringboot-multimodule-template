/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package testcase.small.net

import com.github.francescojo.lib.annotation.SmallTest
import com.github.francescojo.lib.net.pathSegments
import io.kotest.matchers.shouldBe
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.net.URI
import java.util.stream.Stream

/**
 * @since 2022-02-08
 */
@SmallTest
internal class UriUtilsTest {
    @ParameterizedTest(name = "\"{0}\" is separated into: {1}")
    @MethodSource("testSplitPathSegments")
    fun `Uris with slash will be separated correctly`(uriStr: String, expected: List<String>) {
        // given:
        val uri = URI.create(uriStr)

        // when:
        val actual = uri.pathSegments()

        // then:
        actual shouldBe expected
    }

    companion object {
        @JvmStatic
        @Suppress("unused")
        fun testSplitPathSegments(): Stream<Arguments> {
            return Stream.of(
                Arguments.of("/", listOf("/")),
                Arguments.of("http://www.acme.com", emptyList<String>()),
                Arguments.of("http://www.acme.com/", listOf("/")),
                Arguments.of(
                    "http://www.acme.com/groups/00000000-0000-0000-0000-000000000000",
                    listOf("groups", "00000000-0000-0000-0000-000000000000")
                ),
                Arguments.of(
                    "http://www.acme.com/groups/00000000-0000-0000-0000-000000000000/",
                    listOf("groups", "00000000-0000-0000-0000-000000000000")
                )
            )
        }
    }
}
