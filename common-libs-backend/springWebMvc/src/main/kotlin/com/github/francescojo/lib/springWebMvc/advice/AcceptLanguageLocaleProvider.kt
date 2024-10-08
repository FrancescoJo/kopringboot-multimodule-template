/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.springWebMvc.advice

import com.github.francescojo.lib.i18n.LocaleProvider
import java.util.*

/**
 * @since 2022-02-14
 */
internal class AcceptLanguageLocaleProvider(
    acceptLanguage: String?,
    defaultLocales: Set<Locale> = SUPPORTED_LOCALES
) : LocaleProvider {
    private val _locale: Locale = acceptLanguage?.let {
        // Convert  _(underscore) to -(dash) to conform BCP 47 format since Java locale code does not follow it
        val maybeBcp47Format = it.replace("_", "-")

        @Suppress("SwallowedException")
        return@let try {
            Locale.LanguageRange.parse(maybeBcp47Format)
        } catch (e: IllegalArgumentException) {
            null
        }?.run {
            Locale.lookup(this, defaultLocales) ?: DEFAULT
        }
    } ?: DEFAULT

    override val locale: Locale
        get() = _locale

    companion object {
        val DEFAULT: Locale = Locale.ENGLISH

        private val SUPPORTED_LOCALES = setOf(
            Locale.ENGLISH,
        )
    }
}
