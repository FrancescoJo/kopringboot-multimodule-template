/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package testcase.small.util

import com.github.francescojo.lib.annotation.SmallTest
import com.github.francescojo.lib.util.SemanticVersion
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.random.Random

/**
 * Test rules are from:
 * [Backus–Naur Form Grammar for Valid SemVer Versions](https://semver.org/#backusnaur-form-grammar-for-valid-semver-versions)
 *
 * @since 2022-06-18
 */
@SmallTest
internal class SemanticVersionSpec {
    @DisplayName("Semantic Version should be:")
    @Nested
    inner class SemanticVersionShouldBe {
        @Test
        fun `Only version core is displayed if there are no metadata`() {
            // given:
            val (major, minor, patch) = Triple(1, 0, 0)
            val version = SemanticVersion(major, minor, patch).toString()

            // expect:
            version.isExpectedTo("$major.$minor.$patch")
        }

        @Test
        fun `Only version core and preRelease are displayed if there is no build`() {
            // given:
            val (major, minor, patch) = Triple(1, 0, 0)
            val preRelease = "alpha-8"
            val version = SemanticVersion(major, minor, patch, preRelease).toString()

            // expect:
            version.isExpectedTo("$major.$minor.$patch-$preRelease")
        }

        @Test
        fun `Only version core and build are displayed if there is no preRelease`() {
            // given:
            val (major, minor, patch) = Triple(1, 0, 0)
            val build = "09abcdef"
            val version = SemanticVersion(major, minor, patch, "", build).toString()

            // expect:
            version.isExpectedTo("$major.$minor.$patch+$build")
        }

        @Test
        fun `Version and metadata are displayed`() {
            // given:
            val (major, minor, patch) = Triple(1, 0, 0)
            val preRelease = "alpha-8"
            val build = "09abcdef"
            val version = SemanticVersion(major, minor, patch, preRelease, build).toString()

            // expect:
            version.isExpectedTo("$major.$minor.$patch-$preRelease+$build")
        }

        private fun String.isExpectedTo(expected: String) {
            assertAll(
                { this shouldBe expected },
                { SemanticVersion.isParsable(this) shouldBe true },
            )
        }
    }

    @DisplayName("When parsing an arbitrary String to SemanticVersion:")
    @Nested
    inner class WhileParsingStringToSemanticVersion {
        @Test
        fun `Parses to Semantic Version if given string conforms in Semantic Version Pattern`() {
            // given:
            val (major, minor, patch) = Triple(
                Random.nextInt(0, Int.MAX_VALUE),
                Random.nextInt(0, Int.MAX_VALUE),
                Random.nextInt(0, Int.MAX_VALUE)
            )
            val preRelease = "abcde"
            val build = "abcde"

            // when:
            val semanticVersion = SemanticVersion.parse("${major}.${minor}.${patch}-${preRelease}+${build}")!!

            // expect:
            assertAll(
                { semanticVersion.major shouldBe major },
                { semanticVersion.minor shouldBe minor },
                { semanticVersion.patch shouldBe patch },
                { semanticVersion.preRelease shouldBe preRelease },
                { semanticVersion.build shouldBe build }
            )
        }

        @ParameterizedTest
        @MethodSource("testcase.small.util.SemanticVersionSpec#invalidSemanticVersion")
        fun `Parses to null if given string does not conforms in Semantic Version Pattern`(
            invalidSemanticVersion: String
        ) {
            // given:
            val semanticVersion = SemanticVersion.parse(invalidSemanticVersion)

            // expect:
            semanticVersion shouldBe null
        }

        @Test
        fun `Returns true if given string conforms in Semantic Version Pattern`() {
            // given:
            val (major, minor, patch) = Triple(
                Random.nextInt(0, Int.MAX_VALUE),
                Random.nextInt(0, Int.MAX_VALUE),
                Random.nextInt(0, Int.MAX_VALUE)
            )
            val preRelease = "abcde"
            val build = "abcde"

            // when:
            val result =
                SemanticVersion.isParsable("${major}.${minor}.${patch}-${preRelease}+${build}")

            // expect:
            result shouldBe true
        }

        @ParameterizedTest
        @MethodSource("testcase.small.util.SemanticVersionSpec#invalidSemanticVersion")
        fun `Parses to false if given string does not conforms in Semantic Version Pattern`(invalidSemanticVersion: String) {
            // given:
            val isValidSemVer = SemanticVersion.isParsable(invalidSemanticVersion)

            // expect:
            isValidSemVer shouldBe false
        }
    }

    companion object {
        @JvmStatic
        fun invalidSemanticVersion(): Stream<Arguments> = Stream.of(
            Arguments.of("12.3"),
            Arguments.of("1.43"),
            Arguments.of("145"),
            Arguments.of("a.6.7"),
            Arguments.of("6.a.4"),
            Arguments.of("8.66.a"),
            Arguments.of("abcde"),
            Arguments.of("a.b.c"),
            Arguments.of("1.2.1.1-a"),
            Arguments.of("1.2.1-+")
        )
    }
}
