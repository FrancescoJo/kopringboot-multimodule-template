/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.crypto

import java.security.MessageDigest

/**
 * @since 2022-02-14
 */
interface HashingMixin {
    /**
     * Convenient syntactic sugar to hash given String to SHA-1 hashed Byte array.
     */
    fun String.toSha1Bytes(): ByteArray = MessageDigest.getInstance("SHA-1").run {
        digest(this@toSha1Bytes.toByteArray())
    }

    /**
     * Convenient syntactic sugar to hash given String to SHA-256 hashed Byte array.
     */
    fun String.toSha256Bytes(): ByteArray = MessageDigest.getInstance("SHA-256").run {
        digest(this@toSha256Bytes.toByteArray())
    }
}
