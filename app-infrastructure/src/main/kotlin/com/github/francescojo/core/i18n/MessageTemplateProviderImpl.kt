/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.i18n

import com.github.francescojo.core.exception.ErrorCodes
import com.github.francescojo.core.exception.ErrorCodes.*
import com.github.francescojo.lib.annotation.VisibleForTesting
import org.springframework.stereotype.Service
import java.util.*

/**
 * @since 2021-08-10
 */
@Service
internal class MessageTemplateProviderImpl : MessageTemplateProvider {
    override fun provide(locale: Locale, messageKey: String): MessageTemplate? =
        messages[messageKey]?.let { messageMap ->
            locale.allLocaleSpecs().forEach {
                if (messageMap.containsKey(it)) {
                    return messageMap[it]
                }
            }

            return@let null
        }

    /*
     * ISO 638-1 Language codes
     * https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes
     *
     * Language script codes
     * http://unicode.org/iso15924/iso15924-codes.html
     *
     * ISO 3166 ALPHA-2 country codes
     * https://www.iso.org/obp/ui/#search/code/
     */
    internal data class LocaleSpec(
        val language: String,
        val script: String = "",
        val country: String = ""
    )

    internal data class MessageTemplateImpl(
        override val locale: Locale,
        override val key: String,
        override val rawMessage: String
    ) : MessageTemplate

    @VisibleForTesting
    internal var messages = HashMap<String, Map<LocaleSpec, MessageTemplate>>().apply {
        english(SERVICE_NOT_FOUND, "''{0}''  is not a valid input value.")
        english(WRONG_PRESENTATION, "Requested data presentation is not supported.")
        english(WRONG_INPUT, "''{0}''  is not a valid input value.")
        english(MALFORMED_INPUT, "Malformed input.")
        english(GENERAL_HTTP_EXCEPTION, "An unhandled HTTP exception(status = {0}) is occurred.")

        english(UNHANDLED_EXCEPTION, "Unhandled internal error.")
    }

    private fun MutableMap<String, Map<LocaleSpec, MessageTemplate>>.english(key: ErrorCodes, message: String) {
        putMessage(Locale.ENGLISH, key.asMessageKey, message)
    }

    @VisibleForTesting
    internal fun MutableMap<String, Map<LocaleSpec, MessageTemplate>>.putMessage(
        locale: Locale,
        key: String,
        message: String
    ) {
        put(key, LinkedHashMap<LocaleSpec, MessageTemplate>().apply {
            locale.let { lc ->
                locale.allLocaleSpecs().forEach {
                    put(it, MessageTemplateImpl(lc, key, message))
                }
            }
        })
    }

    internal fun Locale.allLocaleSpecs() = listOf(
        LocaleSpec(
            language.lowercase(),
            script.lowercase(),
            country.lowercase()
        ),
        LocaleSpec(language.lowercase(), country.lowercase()),
        LocaleSpec(language.lowercase()),
    )
}
