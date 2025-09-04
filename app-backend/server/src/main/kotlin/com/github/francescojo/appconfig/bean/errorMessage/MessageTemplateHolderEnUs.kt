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
internal class MessageTemplateHolderEnUs : MessageTemplateHolder() {
    override val locale: Locale = Locale.of("en", "US")

    override fun getTemplateOf(code: ErrorCodes): String = when (code) {
        ErrorCodes.SERVICE_NOT_FOUND -> "The requested service ''{0}'' could not be found."
        ErrorCodes.RESOURCE_NOT_FOUND -> "The requested resource ''{0}'' could not be found."
        ErrorCodes.WRONG_PRESENTATION -> "The data format ''{0}'' is not supported."
        ErrorCodes.WRONG_INPUT -> "Invalid input provided: ''{0}''"
        ErrorCodes.MALFORMED_INPUT -> "The input format is invalid: ''{0}''"
        ErrorCodes.EXCEPTION_ON_TRANSMISSION -> "An unexpected error occurred during data transmission. Error code: {0}"
        ErrorCodes.USER_BY_ID_NOT_FOUND -> "No user found with ID ''{0}''."
        ErrorCodes.USER_BY_EMAIL_DUPLICATED -> "A user with the email address ''{0}'' already exists."
        ErrorCodes.USER_BY_NICKNAME_DUPLICATED -> "A user with the nickname ''{0}'' already exists."
        ErrorCodes.DATA_INTEGRITY_BROKEN -> "Data integrity violation detected. The operation could not be completed."
        ErrorCodes.UNHANDLED_EXCEPTION -> "Unhandled internal error. We will thank you for reporting this."
    }
}
