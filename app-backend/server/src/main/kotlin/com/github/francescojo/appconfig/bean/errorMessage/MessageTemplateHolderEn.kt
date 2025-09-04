/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.appconfig.bean.errorMessage

import com.github.francescojo.core.exception.ErrorCodes
import java.util.*

/**
 * @since 2025-09-04
 */
internal class MessageTemplateHolderEn : MessageTemplateHolder() {
    override val locale: Locale = Locale.of("en")

    override fun getTemplateOf(code: ErrorCodes): String = when (code) {
        ErrorCodes.SERVICE_NOT_FOUND -> "Service is not found: ''{0}''"
        ErrorCodes.RESOURCE_NOT_FOUND -> "Resource is not found: ''{0}''"
        ErrorCodes.WRONG_PRESENTATION -> "Data presentation is not supported: ''{0}''"
        ErrorCodes.WRONG_INPUT -> "Invalid input: ''{0}''"
        ErrorCodes.MALFORMED_INPUT -> "Malformed input: ''{0}''"
        ErrorCodes.EXCEPTION_ON_TRANSMISSION -> "An unhandled error is occurred during transmission. Code: {0}"
        ErrorCodes.USER_BY_ID_NOT_FOUND -> "User is not found. ID: ''{0}''"
        ErrorCodes.USER_BY_EMAIL_DUPLICATED -> "User exists with email: ''{0}''"
        ErrorCodes.USER_BY_NICKNAME_DUPLICATED -> "User exists with nickname: ''{0}''"
        ErrorCodes.DATA_INTEGRITY_BROKEN -> "Data integrity violation detected. The operation could not be completed."
        ErrorCodes.UNHANDLED_EXCEPTION -> "Unhandled internal error. We will thank you for reporting this."
    }
}
