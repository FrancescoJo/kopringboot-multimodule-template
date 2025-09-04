package com.github.francescojo.lib.webApi.advice.errorHandler

import com.github.francescojo.core.exception.KopringException
import java.util.Locale

/**
 * @since 2025-09-04
 */
interface ErrorMessageTranslator {
    fun translate(locale: Locale, exception: KopringException): String
}
