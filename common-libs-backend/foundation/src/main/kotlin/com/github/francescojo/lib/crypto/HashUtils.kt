/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.crypto

import java.security.MessageDigest

/**
 * @since 2022-02-14
 */
object HashUtils {
    /**
     * Convenient syntactic sugar to hash given String to SHA-1 hashed Byte array.
     */
    fun stringToSha1Bytes(value: String): ByteArray = MessageDigest.getInstance("SHA-1").run {
        digest(value.toByteArray())
    }

    /**
     * Convenient syntactic sugar to hash given String to SHA-256 hashed Byte array.
     */
    fun stringToSha256Bytes(value: String): ByteArray = MessageDigest.getInstance("SHA-256").run {
        digest(value.toByteArray())
    }
}
