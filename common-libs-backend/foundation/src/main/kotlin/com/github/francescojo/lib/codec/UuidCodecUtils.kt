/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.codec

import java.nio.ByteBuffer
import java.util.*

/**
 * @since 2022-02-14
 */
object UuidCodecUtils {
    /**
     * Converts [UUID] to byte array representation. For example:
     *
     * ```
     * UUID("0f44de8d-cc10-408c-a424-7e3a0328fab0").toByteArray =
     *   [0x0f, 0x44, 0xde, 0x8d, 0xcc, 0x10, 0x40, 0x8c, 0xa4, 0x24, 0x7e, 0x3a, 0x03, 0x28, 0xfa, 0xb0]
     * ```
     */
    fun uuidToByteArray(value: UUID): ByteArray {
        @Suppress("MagicNumber")
        val bb: ByteBuffer = ByteBuffer.wrap(ByteArray(16))
        bb.putLong(value.mostSignificantBits)
        bb.putLong(value.leastSignificantBits)
        return bb.array()
    }

    /**
     * Converts 16-byte length array to UUID representation. For example:
     *
     * ```
     * [0x0f, 0x44, 0xde, 0x8d, 0xcc, 0x10, 0x40, 0x8c, 0xa4, 0x24, 0x7e, 0x3a, 0x03, 0x28, 0xfa, 0xb0].toByteArray =
     *   UUID("0f44de8d-cc10-408c-a424-7e3a0328fab0")
     * ```
     *
     * Note that this method does not check whether given value conforms a valid UUID or not.
     *
     * @throws IllegalArgumentException array length != 16
     */
    fun byteArrayToUUID(value: ByteArray): UUID {
        @Suppress("MagicNumber")
        if (value.size != 16) {
            throw IllegalArgumentException("Only 16-bytes long byte array could be cast to UUID.")
        }

        val byteBuffer: ByteBuffer = ByteBuffer.wrap(value)
        val high: Long = byteBuffer.long
        val low: Long = byteBuffer.long
        return UUID(high, low)
    }
}
