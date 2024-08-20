/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.util

/**
 * A semantic version definition and implementation.
 *
 * [More documentation](https://semver.org/)
 *
 * @since 2022-05-20
 */
data class SemanticVersion(
    val major: Int,
    val minor: Int,
    val patch: Int,
    val preRelease: String = "",
    val build: String = ""
) : Comparable<SemanticVersion> {
    override fun toString(): String {
        val pre = preRelease.takeIf { it.isNotEmpty() }?.let { "-$it" } ?: ""
        val build = build.takeIf { it.isNotEmpty() }?.let { "+$it" } ?: ""

        return "$major.$minor.$patch$pre$build"
    }

    override fun compareTo(other: SemanticVersion): Int {
        return compareValuesBy(this, other, { it.major }, { it.minor }, { it.patch })
    }

    companion object {
        // https://semver.org/#is-there-a-suggested-regular-expression-regex-to-check-a-semver-string
        const val PATTERN =
            "^(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)" +
                    "(?:-((?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\\.(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?" +
                    "(?:\\+([0-9a-zA-Z-_]+(?:\\.[0-9a-zA-Z-]+)*))?$"

        fun parse(semVer: String?): SemanticVersion? =
            Regex(PATTERN)
                .matchEntire(semVer ?: "")?.destructured?.let { (major, minor, patch, preRelease, build) ->
                    SemanticVersion(
                        major = major.toInt(),
                        minor = minor.toInt(),
                        patch = patch.toInt(),
                        preRelease = preRelease,
                        build = build
                    )
                }

        fun isParsable(semVer: String?): Boolean =
            Regex(PATTERN).matches(semVer ?: "")
    }
}
