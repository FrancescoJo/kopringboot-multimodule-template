/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.exception

/**
 * @since 2021-08-10
 */
enum class ErrorCodes(
    val code: Long,
    val description: String
) {
    SERVICE_NOT_FOUND(code = 10, ""),
    RESOURCE_NOT_FOUND(code = 20, ""),
    WRONG_PRESENTATION(code = 30, ""),
    WRONG_INPUT(code = 40, ""),
    MALFORMED_INPUT(code = 50, ""),

    EXCEPTION_ON_TRANSMISSION(200, ""),

    USER_BY_ID_NOT_FOUND(1001, ""),
    USER_BY_EMAIL_DUPLICATED(1002, ""),
    USER_BY_NICKNAME_DUPLICATED(1003, ""),

    DATA_INTEGRITY_BROKEN(code = 50000, ""),

    UNHANDLED_EXCEPTION(code = -1, "");

    val asMessageKey: String = "ERROR_${this.name}"

    companion object {
        @JvmStatic
        fun from(code: Any?) =
            entries.firstOrNull { it.code == code?.toString()?.toLongOrNull() } ?: UNHANDLED_EXCEPTION
    }
}
