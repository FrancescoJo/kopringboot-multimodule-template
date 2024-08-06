/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.i18n

import java.util.*

/**
 * @since 2021-08-10
 */
interface MessageTemplateProvider {
    fun provide(locale: Locale, messageKey: String): MessageTemplate?
}
