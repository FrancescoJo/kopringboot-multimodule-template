/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.jdbc

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreator
import org.springframework.jdbc.core.SqlProvider
import org.springframework.jdbc.core.StatementCreatorUtils
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.Timestamp
import java.sql.Types
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

/**
 * @since 2021-08-10
 */
@Suppress("TooManyFunctions")  // Code sharing for limited range. Ignore it.
internal abstract class JdbcTemplateHelper {
    protected abstract val jdbcTemplate: JdbcTemplate

    fun doInsertAndGetId(
        idColumn: String,
        sql: String,
        preparedStatementSetter: PreparedStatement.() -> Unit
    ): KeyHolder = GeneratedKeyHolder().apply {
        jdbcTemplate.update(
            preparedStatementCreator(sql, arrayOf(idColumn)) { preparedStatementSetter(this) },
            this
        )
    }

    fun doUpdate(
        idColumn: String,
        sql: String,
        preparedStatementSetter: PreparedStatement.() -> Unit
    ): Int = jdbcTemplate.update(
        preparedStatementCreator(sql, arrayOf(idColumn)) { preparedStatementSetter(this) }
    )

    // region PreparedStatement utility methods
    fun PreparedStatement.setBinaryEx(index: Int, value: ByteArray?, sqlType: Int = Types.BINARY) {
        StatementCreatorUtils.setParameterValue(this, index, sqlType, value)
    }

    fun PreparedStatement.setStringEx(index: Int, value: String?, sqlType: Int = Types.VARCHAR) {
        StatementCreatorUtils.setParameterValue(this, index, sqlType, value)
    }

    fun PreparedStatement.setLongEx(index: Int, value: Long?, sqlType: Int = Types.BIGINT) {
        StatementCreatorUtils.setParameterValue(this, index, sqlType, value)
    }

    fun PreparedStatement.setBooleanEx(index: Int, value: Boolean?, sqlType: Int = Types.BOOLEAN) {
        StatementCreatorUtils.setParameterValue(this, index, sqlType, value)
    }

    fun PreparedStatement.setTimestampEx(index: Int, value: Instant?, sqlType: Int = Types.TIMESTAMP) {
        setTimestampEx(index, value?.toTimestamp(), sqlType)
    }

    fun PreparedStatement.setTimestampEx(index: Int, value: Timestamp?, sqlType: Int = Types.TIMESTAMP) {
        StatementCreatorUtils.setParameterValue(this, index, sqlType, value)
    }
    // endregion

    // region Type coercion utility methods
    fun Instant.toTimestamp(): Timestamp = Timestamp.from(this)

    fun LocalDateTime.toInstant(): Instant = this.toInstant(ZoneOffset.UTC)

    /**
     * Utility method to treat different SQL DATETIME conversion policies by storage engine and JDBC Drivers.
     */
    fun Any.coerceToInstant(): Instant = when (this) {
        is Timestamp -> this.toInstant()
        is LocalDateTime -> this.toInstant()
        else -> throw ClassCastException("Cannot convert ${this::class} ${Instant::class}")
    }
    // endregion

    private fun preparedStatementCreator(
        sql: String,
        returningColumnNames: Array<String>,
        pss: PreparedStatement.() -> Unit
    ): PreparedStatementCreator {
        return object : PreparedStatementCreator, SqlProvider {
            override fun createPreparedStatement(connection: Connection): PreparedStatement =
                connection.prepareStatement(sql, returningColumnNames).apply { pss(this) }

            override fun getSql(): String = sql
        }
    }
}
