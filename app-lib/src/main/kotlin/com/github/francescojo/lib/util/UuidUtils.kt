/*
 * sirloin-jvmlib
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.util

import java.nio.ByteBuffer
import java.util.UUID

/**
 * A 'nil' UUID for emptiness checks. UUID value: "00000000-0000-0000-0000-000000000000"
 */
val EMPTY_UUID = UUID(0, 0)

private const val REGEX_HEXADECIMAL = "[0-9a-fA-F]"
/**
 * Matches all UUID patterns through version 1 to 5. Note that JVM [UUID] implementation uses UUID v4 by default.
 */
const val REGEX_UUID = "$REGEX_HEXADECIMAL{8}-" +
        "$REGEX_HEXADECIMAL{4}-" +
        "[1-5]$REGEX_HEXADECIMAL{3}-" +
        "[89AaBb]$REGEX_HEXADECIMAL{3}-" +
        "$REGEX_HEXADECIMAL{12}"

/**
 * Converts [UUID] to byte array representation. For example:
 *
 * ```
 * UUID("0f44de8d-cc10-408c-a424-7e3a0328fab0").toByteArray =
 *   [0x0f, 0x44, 0xde, 0x8d, 0xcc, 0x10, 0x40, 0x8c, 0xa4, 0x24, 0x7e, 0x3a, 0x03, 0x28, 0xfa, 0xb0]
 * ```
 *
 * @since 2022-02-14
 */
fun UUID.toByteArray(): ByteArray {
    @Suppress("MagicNumber")
    val bb: ByteBuffer = ByteBuffer.wrap(ByteArray(16))
    bb.putLong(mostSignificantBits)
    bb.putLong(leastSignificantBits)
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
 * @since 2022-02-14
 */
fun ByteArray.toUUID(): UUID {
    @Suppress("MagicNumber")
    if (this.size != 16) {
        throw IllegalArgumentException("Only 16-bytes long byte array could be cast to UUID.")
    }

    val byteBuffer: ByteBuffer = ByteBuffer.wrap(this)
    val high: Long = byteBuffer.long
    val low: Long = byteBuffer.long
    return UUID(high, low)
}
