/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.i18n

import java.text.MessageFormat
import java.util.*

/**
 * @since 2021-08-10
 */
interface MessageTemplate {
    val locale: Locale

    val key: String

    val rawMessage: String

    fun format(args: Collection<Any>): String {
        val message = if (args.isEmpty()) {
            rawMessage
        } else {
            @Suppress("SwallowedException")     // Not a fatal error, so we swallow this exception.
            try {
                @Suppress("SpreadOperator")     // How can we solve this problem without using spread operator?
                // https://youtrack.jetbrains.com/issue/KT-12663
                MessageFormat.format(rawMessage, *(args.toTypedArray()))
            } catch (ignore: IllegalArgumentException) {
                rawMessage
            }
        }

        return message.trim()
    }
}
