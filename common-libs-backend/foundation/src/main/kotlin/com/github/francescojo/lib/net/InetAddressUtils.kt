/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.net

import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress

/**
 * IPv4 to IPv6 rule is from
 * [RFC 4291](https://tools.ietf.org/html/rfc4291#section-2.5.5.2).
 *
 * @since 2022-02-14
 */
fun InetAddress.toIpV6AddressBytes(): ByteArray {
    @Suppress("MagicNumber")
    return when (this) {
        is Inet4Address -> byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0xFF.toByte(), 0xFF.toByte(),
            address[0], address[1], address[2], address[3]
        )
        is Inet6Address -> this.address
    }
}

/**
 * Converts given [kotlin.ByteArray] which may represents IPv4 or IPv6 address,
 * to [java.net.InetAddress] representation.
 *
 * The operating [kotlin.ByteArray] size must be either 4 or 16.
 *
 * @since 2022-02-14
 */
fun ByteArray.toInetAddress(): InetAddress {
    @Suppress("MagicNumber")
    return if (size != 4 && size != 16) {
        InetAddresses.EMPTY
    } else {
        InetAddress.getByAddress(this)
    }
}

/**
 * Determines that operating [java.net.InetAddress] represents whether an IPv4 address or not.
 *
 * @since 2022-02-14
 */
fun InetAddress.isIpV4Address(): Boolean {
    return this is Inet4Address
}

object InetAddresses {
    /**
     * Workaround for
     *
     * ```
     * fun InetAddress.Companion.empty(): InetAddress = EMPTY_INET_ADDRESS
     * ```
     *
     * which is currently(Kotlin 1.0) impossible.
     * Read [Kotlin Youtrack issues](https://youtrack.jetbrains.com/issue/KT-11968) for more information.
     *
     * @since 2022-02-14
     */
    val EMPTY: InetAddress = InetAddress.getByName("0.0.0.0")

    val LOCALHOST: InetAddress = InetAddress.getByName("localhost")
}

fun InetAddress?.isNullOrEmpty(): Boolean = this == null || this == InetAddresses.EMPTY
