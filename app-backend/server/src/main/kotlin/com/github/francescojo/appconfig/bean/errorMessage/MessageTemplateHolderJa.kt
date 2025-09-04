/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.appconfig.bean.errorMessage

import com.github.francescojo.core.exception.ErrorCodes
import java.util.Locale

/**
 * @since 2025-09-04
 */
internal class MessageTemplateHolderJa : MessageTemplateHolder() {
    override val locale: Locale = Locale.of("ja")

    override fun getTemplateOf(code: ErrorCodes): String = when (code) {
        ErrorCodes.SERVICE_NOT_FOUND -> ""
        ErrorCodes.RESOURCE_NOT_FOUND -> ""
        ErrorCodes.WRONG_PRESENTATION -> ""
        ErrorCodes.WRONG_INPUT -> ""
        ErrorCodes.MALFORMED_INPUT -> ""
        ErrorCodes.EXCEPTION_ON_TRANSMISSION -> ""
        ErrorCodes.USER_BY_ID_NOT_FOUND -> ""
        ErrorCodes.USER_BY_EMAIL_DUPLICATED -> ""
        ErrorCodes.USER_BY_NICKNAME_DUPLICATED -> ""
        ErrorCodes.DATA_INTEGRITY_BROKEN -> ""
        ErrorCodes.UNHANDLED_EXCEPTION -> "未処理の内部エラーが発生しました。ご報告いただきありがとうございます。"
    }
}
