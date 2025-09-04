/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.appconfig.bean.errorMessage

import com.github.francescojo.core.exception.ErrorCodes
import java.util.Locale

/**
 * All implementations of `MessageTemplateHolder` must have a no-argument constructor.
 *
 * @since 2025-09-04
 */
internal sealed class MessageTemplateHolder() {
    abstract val locale: Locale

    abstract fun getTemplateOf(code: ErrorCodes): String
}
