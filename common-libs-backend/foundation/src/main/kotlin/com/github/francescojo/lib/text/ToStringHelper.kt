/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.text

/**
 * @since 2024-08-23
 */
object ToStringHelper {
    fun prettyToString(
        toStringTarget: Any,
        attributes: Map<String, Any?>,
        printSignature: Boolean = true,
        style: ToStringStyle = ToStringStyle.json()
    ): String = style.prettyToString(toStringTarget, attributes, printSignature)

    // region SPI Connectors
    var jsonToStringifier: ((Any, Map<String, Any?>) -> String)? = null
    // endregion
}
