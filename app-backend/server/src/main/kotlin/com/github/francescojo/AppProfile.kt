/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo

/**
 * @since 2021-08-10
 */
enum class AppProfile(val profileName: String) {
    LOCAL(profileName = "local"),
    ALPHA(profileName = "alpha"),
    BETA(profileName = "beta"),
    RELEASE(profileName = "release"),
    TEST(profileName = "test");

    companion object {
        fun from(
            profileName: String?,
            defaultValue: AppProfile = LOCAL
        ) = values().firstOrNull { it.profileName == profileName } ?: defaultValue
    }
}
