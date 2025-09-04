/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.appconfig.bean.errorMessage

import com.github.francescojo.core.exception.ErrorCodes
import com.github.francescojo.core.exception.KopringException
import com.github.francescojo.lib.annotation.VisibleForTesting
import com.github.francescojo.lib.lang.ReflectionUtils.findAllConcreteImplementationsOf
import com.github.francescojo.lib.webApi.advice.errorHandler.ErrorMessageTranslator
import jakarta.annotation.PostConstruct
import org.slf4j.Logger
import org.springframework.stereotype.Component
import java.text.MessageFormat
import java.util.*

/**
 * @since 2025-09-04
 */
@Component
internal class ErrorMessageTranslatorImpl(
    private val log: Logger
) : ErrorMessageTranslator {
    @VisibleForTesting
    internal val implementations: MutableMap<LocaleAsKey, MessageTemplateHolder> = mutableMapOf()

    @PostConstruct
    fun onPostConstruct() {
        val implClasses = findAllConcreteImplementationsOf(MessageTemplateHolder::class)

        for (implClass in implClasses) {
            val instance =
                implClass.constructors.find { it.parameters.isEmpty() }?.call() ?: throw UnsupportedOperationException(
                    "Cannot instantiate ${implClass.qualifiedName}. A no-argument constructor is required."
                )

            val key = LocaleAsKey.from(instance.locale)

            with(implementations[key]) {
                if (this == null) {
                    implementations[key] = instance
                    log.trace("ErrorMessageTranslator[{}] = {}", instance.locale, implClass.qualifiedName)
                } else {
                    throw IllegalStateException(
                        "Duplicate MessageTemplateHolder for locale '${instance.locale}': " +
                                "${this::class.qualifiedName} and ${implClass.qualifiedName}"
                    )
                }
            }
        }
    }

    override fun translate(locale: Locale, exception: KopringException): String {
        val requestedKey = LocaleAsKey.from(locale)

        val template = if (exception.errorCode is ErrorCodes) {
            findBestMessageTemplateHolder(requestedKey)?.getTemplateOf(exception.errorCode as ErrorCodes)
        } else {
            null
        }

        return (template?.run { exception.translateWithTemplate(this) }
            ?: exception.errorCode.defaultMessage).takeIf { it.isNotEmpty() } ?: exception.message
    }

    private fun findBestMessageTemplateHolder(requestedKey: LocaleAsKey): MessageTemplateHolder? {
        val candidateKeys = listOfNotNull(
            // Priority 1: Exact match (language + script + country)
            requestedKey,

            // Priority 2: Language + Country match (ignore script)
            requestedKey.country?.let { LocaleAsKey(requestedKey.language, null, it) },

            // Priority 3: Language-only match (ignore both script and country)
            LocaleAsKey(requestedKey.language, null, null)
        )

        val bestCandidate = candidateKeys.firstNotNullOfOrNull { key ->
            implementations[key]
        }

        return bestCandidate ?: implementations[DEFAULT_LANGUAGE_KEY]
    }

    private fun KopringException.translateWithTemplate(template: String): String =
        if (messageArguments().isNullOrEmpty()) {
            if (template.contains(Regex("\\{\\d+\\}")) || template.contains("{}")) {
                errorCode.defaultMessage
            } else {
                template
            }
        } else {
            // Not a fatal error, so we swallow this exception.
            @Suppress("SwallowedException")
            try {
                // https://youtrack.jetbrains.com/issue/KT-12663
                @Suppress("SpreadOperator")
                /* return try */ MessageFormat.format(template, *(messageArguments()!!.toTypedArray()))
            } catch (_: IllegalArgumentException) {
                /* return try */ errorCode.defaultMessage
            }
        }

    internal data class LocaleAsKey(
        val language: String,
        val script: String?,
        val country: String?
    ) {
        companion object {
            fun from(locale: Locale): LocaleAsKey = LocaleAsKey(
                language = locale.language,
                script = locale.script.ifEmpty { null },
                country = locale.country.ifEmpty { null }
            )
        }
    }

    companion object {
        private val DEFAULT_LANGUAGE_KEY = LocaleAsKey("en", null, null)
    }
}
