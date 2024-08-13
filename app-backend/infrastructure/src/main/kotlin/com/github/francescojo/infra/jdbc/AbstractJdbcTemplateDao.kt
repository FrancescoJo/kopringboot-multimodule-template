/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.infra.jdbc

import com.github.francescojo.infra.jdbc.common.Versioned
import com.github.francescojo.infra.jdbc.util.JdbcSqlHelperMixin
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreator
import org.springframework.jdbc.core.SqlProvider
import org.springframework.jdbc.core.StatementCreatorUtils
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import java.math.BigDecimal
import java.math.BigInteger
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.Timestamp
import java.sql.Types
import java.time.Instant

/**
 * @since 2021-08-10
 */
@Suppress("TooManyFunctions", "UNCHECKED_CAST")  // Code sharing for limited range. Ignore it.
internal abstract class AbstractJdbcTemplateDao(
    private val jdbcTemplate: JdbcTemplate
) : JdbcSqlHelperMixin {
    // region PreparedStatement utility methods
    protected fun PreparedStatement.setBinaryEx(index: Int, value: ByteArray?) {
        StatementCreatorUtils.setParameterValue(this, index, Types.BINARY, value)
    }

    protected fun PreparedStatement.setStringEx(index: Int, value: String?) {
        StatementCreatorUtils.setParameterValue(this, index, Types.VARCHAR, value)
    }

    protected fun PreparedStatement.setIntEx(index: Int, value: Int?) {
        StatementCreatorUtils.setParameterValue(this, index, Types.INTEGER, value)
    }

    protected fun PreparedStatement.setLongEx(index: Int, value: Long?) {
        StatementCreatorUtils.setParameterValue(this, index, Types.BIGINT, value)
    }

    protected fun PreparedStatement.setDoubleEx(index: Int, value: Double?) {
        StatementCreatorUtils.setParameterValue(this, index, Types.DOUBLE, value)
    }

    protected fun PreparedStatement.setBooleanEx(index: Int, value: Boolean?) {
        StatementCreatorUtils.setParameterValue(this, index, Types.BOOLEAN, value)
    }

    protected fun PreparedStatement.setTimestampEx(index: Int, value: Instant?) {
        setTimestampEx(index, value?.toTimestamp())
    }

    protected fun PreparedStatement.setTimestampEx(index: Int, value: Timestamp?) {
        StatementCreatorUtils.setParameterValue(this, index, Types.TIMESTAMP, value)
    }

    protected fun PreparedStatement.setBigIntegerEx(index: Int, value: BigInteger) {
        StatementCreatorUtils.setParameterValue(this, index, Types.BIGINT, value)
    }

    protected fun PreparedStatement.setBigDecimalEx(index: Int, value: BigDecimal) {
        StatementCreatorUtils.setParameterValue(this, index, Types.DECIMAL, value)
    }
    // endregion

    // region high-level CRUD operations
    // region CREATE
    protected fun <T : Any> insertAll(
        values: Collection<T>,
        table: String,
        valueToRowMapper: (T) -> Map<String, Any?>
    ): List<T> =
        insertAllAndGetKeyInternal<Nothing, T>(values, table, valueToRowMapper, "", null)

    protected fun <K : Number, T : Any> insertAllAndGetKey(
        entities: Collection<T>,
        table: String,
        valueToRowMapper: (T) -> Map<String, Any?>,
        keyColumn: String,
        onKeyGenerated: ((K, T) -> Unit)
    ): List<T> =
        insertAllAndGetKeyInternal(entities, table, valueToRowMapper, keyColumn, onKeyGenerated)

    private fun <K : Number, T : Any> insertAllAndGetKeyInternal(
        entities: Collection<T>,
        table: String,
        valueToRowMapper: (T) -> Map<String, Any?>,
        keyColumn: String,
        onKeyGenerated: ((K, T) -> Unit)? = null
    ): List<T> {
        require(entities.isNotEmpty()) { "Objects to create must not be empty" }
        val columns = valueToRowMapper(entities.first()).keys
        require(columns.isNotEmpty()) { "Entity to Column-Value pair mapper returns a empty mapping rule" }

        if (onKeyGenerated == null) {
            val valuesClause = entities.joinToString(", ") {
                "(${columns.joinToString(", ") { "?" }})"
            }
            val sql = """
                INSERT INTO `$table` (${columns.joinToString(", ")})
                VALUES $valuesClause
            """.trimIndent()

            val results = ArrayList<T>(entities.size)

            doUpdate(columns.first(), sql) {
                var offset = 0
                entities.forEach { entity ->
                    valueToRowMapper(entity).entries.forEachIndexed { i, entry ->
                        setParameterEx((i + 1) + offset, entry.value, table, entry.key)
                    }
                    offset += columns.size
                    results.add(entity)
                }
            }

            return results
        } else {
            // Bulk insert is impossible at here due to collecting AUTO INCREMENT sequence value.
            val sql = """
                INSERT INTO `$table` (${columns.joinToString(", ")})
                VALUES (${columns.joinToString(", ") { "?" }})
            """.trimIndent()

            return entities.map { entity ->
                /*
                 * Checked cast in generic context - ClassCastException occurs at here if there are mistakes at
                 * call sites.
                 */
                @Suppress("UNCHECKED_CAST")
                val key = doInsertAndGetId(keyColumn, sql) {
                    valueToRowMapper(entity).entries.forEachIndexed { i, entry ->
                        setParameterEx(i + 1, entry.value, table, entry.key)
                    }
                }.key as K

                onKeyGenerated(key, entity)

                return@map entity
            }
        }
    }
    // endregion

    // region READ
    protected fun <K : Any, T : Any> selectAllByKeys(
        keys: Collection<K>,
        table: String,
        keyColumn: String,
        moreConditions: List<String> = emptyList(),
        resultConverter: (Map<String, Any?>) -> T,
        lockTables: Boolean
    ): List<T> {
        require(keys.isNotEmpty()) { "Keys to query objects must not be empty" }
        val keySet = keys.toSet()

        var sql = """
            SELECT *
            FROM `$table`
            WHERE `$keyColumn` IN (${keySet.joinToString(", ") { "?" }}) ${moreConditions.joinToString(" ")}
        """.trimIndent()

        if (lockTables) {
            sql += " FOR UPDATE"
        }

        val idAsVarargs: Array<Any> = keySet.run {
            val array = Array<Any?>(size) { null }

            keySet.forEachIndexed { i, id -> array[i] = id }
            /*
             * This is safe cast because the array is initialized with non-null values from 'keySet'
             * which is derived from 'keys'.
             */
            @Suppress("UNCHECKED_CAST")
            return@run array as Array<Any>
        }

        return selectMany(sql, resultConverter, *idAsVarargs)
    }
    // endregion

    // region UPDATE
    protected fun <K : Any, T : Any> updateAllByKeys(
        entities: Collection<T>,
        table: String,
        updateSpec: (T) -> Map<String, Any?>,
        /**
         * Supports only single Key column. You must write your own UPDATE query if there are
         * DAOs that handles a database entity object that has multiple columns as Primary Key.
         */
        keySpec: (T) -> Pair<String, K>,
        autoUpdateVersion: Boolean = true
    ): List<T> {
        require(entities.isNotEmpty()) { "Objects to modify must not be empty" }
        val keyColumn = keySpec(entities.first()).first
        val sampleUpdateSpec = updateSpec(entities.first())
        val sampleColumns = sampleUpdateSpec.keys
        require(sampleColumns.isNotEmpty()) { "Entity to Column-Value pair mapper returns a empty mapping rule" }
        val maybeVersioned = entities.first()

        val enhancedColumns: Set<String>
        val enhancedUpdateSpec: (T) -> Map<String, Any?>
        if (maybeVersioned is Versioned<*> && autoUpdateVersion &&
            !sampleUpdateSpec.containsKey(maybeVersioned.versionColumnName)
        ) {
            enhancedColumns = sampleColumns + maybeVersioned.versionColumnName
            enhancedUpdateSpec = lambda@ {
                val map = LinkedHashMap<String, Any?>().apply {
                    putAll(updateSpec(it))
                    put(maybeVersioned.versionColumnName, (it as Versioned<*>).increase())
                }

                return@lambda map
            }
        } else {
            enhancedColumns = sampleColumns
            enhancedUpdateSpec = updateSpec
        }

        // BULK UPDATE is impossible in 'SAVE' context.
        val sql = """
            UPDATE `$table`
            SET ${enhancedColumns.joinToString(", ") { "`$it` = ?" }}
            WHERE `$keyColumn` = ?
        """.trimIndent()

        val errors = ArrayList<K>()
        val updated = entities.map { entity ->
            entity.apply {
                val (column, value) = keySpec(entity)

                @Suppress("MagicNumber")    // Not magic numbers at here
                val count = doUpdate(keyColumn, sql) {
                    var indices = 0
                    enhancedUpdateSpec(entity).entries.forEachIndexed { i, entry ->
                        setParameterEx(i + 1, entry.value, table, entry.key)
                        ++indices
                    }
                    setParameterEx(indices + 1, value, table, column)
                }

                if (count != 1) {
                    errors.add(value)
                }
            }
        }

        if (errors.isNotEmpty()) {
            throw NoSuchElementException(
                "There is/are no object(s) matching the key(s): $errors, on $table.$keyColumn"
            )
        }

        return updated
    }
    // endregion

    // region DELETE
    protected fun <T : Any> deleteAllByKeys(
        keys: Collection<T>,
        table: String,
        keyColumn: String,
        keyProcessor: (PreparedStatement.(Int, T) -> Unit)? = null
    ): Long {
        require(keys.isNotEmpty()) { "Objects to remove must not be empty" }
        val keySet = keys.toSet()

        val sql = """
            DELETE FROM `$table`
            WHERE `$keyColumn` IN (${keySet.joinToString(", ") { "?" }})
        """.trimIndent()

        return doUpdate(keyColumn, sql) {
            keySet.forEachIndexed { i, id ->
                // i starts from 0; however JDBC API accepts from 1 as index, so we should add 1 here
                val newIdx = i + 1

                if (keyProcessor == null) {
                    setParameterEx(newIdx, id, table, keyColumn)
                } else {
                    keyProcessor(newIdx, id)
                }
            }
        }.toLong()
    }
    // endregion

    private fun PreparedStatement.setParameterEx(index: Int, value: Any?, table: String, column: String) {
        if (value == null) {
            StatementCreatorUtils.setParameterValue(this, index, Types.NULL, null)
            return
        }

        when (value) {
            is ByteArray -> setBinaryEx(index, value)
            is String -> setStringEx(index, value)
            is Int -> setIntEx(index, value)
            is Long -> setLongEx(index, value)
            is Boolean -> setBooleanEx(index, value)
            is Instant -> setTimestampEx(index, value)
            is Double -> setDoubleEx(index, value)
            is Timestamp -> setTimestampEx(index, value)
            is BigInteger -> setBigIntegerEx(index, value)
            is BigDecimal -> setBigDecimalEx(index, value)

            else -> throw IllegalArgumentException("Unsupported value type: ${value::class} on ${table}.$column")
        }
    }
    // endregion

    // region low-level CRUD Operations
    private fun doInsertAndGetId(
        idColumn: String,
        sql: String,
        preparedStatementSetter: PreparedStatement.() -> Unit
    ): KeyHolder = GeneratedKeyHolder().apply {
        jdbcTemplate.update(
            preparedStatementCreator(sql, arrayOf(idColumn)) { preparedStatementSetter(this) },
            this
        )
    }

    private fun <T> selectMany(sql: String, recordConverter: (Map<String, Any?>) -> T, vararg args: Any): List<T> =
        jdbcTemplate.queryForList(sql, *args).map(recordConverter)

    private fun doUpdate(
        idColumn: String,
        sql: String,
        preparedStatementSetter: PreparedStatement.() -> Unit
    ): Int = jdbcTemplate.update(
        preparedStatementCreator(sql, arrayOf(idColumn)) { preparedStatementSetter(this) }
    )

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
    // endregion
}
