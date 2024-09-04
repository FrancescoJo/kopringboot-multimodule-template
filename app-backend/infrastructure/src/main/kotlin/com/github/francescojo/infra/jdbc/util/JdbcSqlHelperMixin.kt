/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.infra.jdbc.util

import com.github.francescojo.lib.codec.UuidCodecUtils.uuidToByteArray
import com.github.francescojo.lib.text.toHexString
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZoneOffset.UTC
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * @since 2024-08-13
 */
interface JdbcSqlHelperMixin {
    fun UUID.toSqlBinaryLiteral(): String = uuidToByteArray(this).toSqlBinaryLiteral()

    fun ByteArray.toSqlBinaryLiteral(): String = "0x${this.toHexString()}"

    fun Instant.toSqlTimestampLiteral(): String =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(UTC).format(this)

    fun Instant.toTimestamp(): Timestamp = Timestamp.from(this)

    fun LocalDateTime.toInstant(): Instant = this.toInstant(ZoneOffset.UTC)

    /**
     * Utility method to treat different SQL DATETIME conversion policies by storage engine and JDBC Drivers.
     */
    fun Any.coerceToInstant(): Instant = when (this) {
        is Timestamp -> this.toInstant()
        is LocalDateTime -> this.toInstant(ZoneOffset.UTC)
        else -> throw ClassCastException("Cannot convert ${this::class} ${Instant::class}")
    }

    fun Array<*>.toInClauseConditions(): String = joinToString(", ") { "?" }

    fun Iterable<*>.toInClauseConditions(): String = joinToString(", ") { "?" }
}
