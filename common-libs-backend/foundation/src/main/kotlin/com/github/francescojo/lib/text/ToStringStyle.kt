/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.text

import com.github.francescojo.lib.text.ToStringHelper.jsonToStringifier
import java.util.*

/**
 * @since 2024-08-23
 */
abstract class ToStringStyle() {
    internal fun prettyToString(value: Any, attributes: Map<String, Any?>, printSignature: Boolean): String {
        val contents = prettyToStringInternal(value, attributes)

        return if (printSignature) {
            val name = value::class.simpleName ?: "?"
            val hash = String.format("%08X", value.hashCode()).lowercase(Locale.US)

            "$name@$hash $contents"
        } else {
            contents
        }
    }

    protected abstract fun prettyToStringInternal(value: Any, attributes: Map<String, Any?>): String

    companion object {
        fun json(
            multiline: Boolean = true,
            indent: Int = 2,
            valueSerialiser: ((Any?) -> String)? = null
        ): ToStringStyle = JsonToStringStyle(
            multiline = multiline,
            indent = indent,
            valueSerialiser = valueSerialiser
        )
    }
}

private class JsonToStringStyle(
    private val multiline: Boolean,
    private val indent: Int,
    private val valueSerialiser: ((Any?) -> String)?
) : ToStringStyle() {
    private val separator: String by lazy { if (multiline) ": " else ":" }

    private fun buildPadding(indent: Int): String = " ".repeat(indent)

    override fun prettyToStringInternal(value: Any, attributes: Map<String, Any?>): String {
        jsonToStringifier?.invoke(this, attributes)?.let { return it }

        return if (multiline) {
            multiLined(attributes, indent, false)
        } else {
            singleLined(attributes)
        }
    }

    private fun multiLined(attributes: Map<String, Any?>, currentIndent: Int, nested: Boolean): String {
        val padding = buildPadding(currentIndent)
        val buffer = StringBuilder()

        if (!nested) {
            buffer.append("{\n")
        }

        val entries = attributes.map { (key, value) ->
            val formattedValue = when (value) {
                is Map<*, *> -> "{\n${multiLined(value.toJsonMap(), currentIndent + indent, true)}\n$padding}"
                is Collection<*> -> value.serialiseCollection(true, currentIndent)
                is Array<*> -> value.serialiseArray(true, currentIndent)
                else -> value.serialiseAny()
            }

            "$padding${"\"$key\""}$separator$formattedValue"
        }

        buffer.append(entries.joinToString(",\n"))

        if (!nested) {
            buffer.append("\n}")
        }

        return buffer.toString()
    }

    private fun singleLined(attributes: Map<String, Any?>): String {
        return attributes.entries.joinToString(",", "{", "}") { (key, value) ->
            val formattedValue = when (value) {
                is Map<*, *> -> singleLined(value.toJsonMap())
                is Collection<*> -> value.serialiseCollection(false, 0)
                is Array<*> -> value.serialiseArray(false, 0)
                else -> value.serialiseAny()
            }

            "${"\"$key\""}$separator$formattedValue"
        }
    }

    private fun Collection<*>.serialiseCollection(
        multiline: Boolean,
        currentIndent: Int
    ): String {
        return if (multiline && this.any { it is Map<*, *> }) {
            joinToString(
                separator = ",\n",
                prefix = "[\n",
                postfix = "\n${buildPadding(currentIndent)}]",
                transform = iterableTransformer(currentIndent)
            )
        } else {
            joinToString(", ", "[", "]") { it.serialiseAny() }
        }
    }

    private fun Array<*>.serialiseArray(
        multiline: Boolean,
        currentIndent: Int
    ): String {
        return if (multiline && this.any { it is Map<*, *> }) {
            joinToString(
                separator = ",\n",
                prefix = "[\n",
                postfix = "\n${buildPadding(currentIndent)}]",
                transform = iterableTransformer(currentIndent)
            )
        } else {
            joinToString(", ", "[", "]") { it.serialiseAny() }
        }
    }

    private fun iterableTransformer(currentIndent: Int): ((Any?) -> CharSequence) {
        val newIndent = currentIndent + indent
        val padding = buildPadding(newIndent)

        return {
            if (it is Map<*, *>) {
                "$padding{\n${multiLined(it.toJsonMap(), newIndent + indent, true)}\n$padding}"
            } else {
                "$padding${it.serialiseAny()}"
            }
        }
    }

    private fun Any?.serialiseAny(): String {
        return when (this) {
            is String -> "\"${this.replace("\n", "\\n").replace("\r", "\\r")}\""
            else -> valueSerialiser?.invoke(this) ?: this.toString()
        }
    }

    @Suppress("UNCHECKED_CAST")     // All casts here are checked
    private fun Map<*, *>.toJsonMap(): Map<String, Any?> {
        if (this.isEmpty()) {
            return this as Map<String, Any?>
        }

        if (this.keys.first() is String) {
            return this as Map<String, Any?>
        }

        return this.mapKeys { it.key.toString() }
    }
}
