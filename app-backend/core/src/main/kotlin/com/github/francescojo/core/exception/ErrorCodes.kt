/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.exception

/**
 * @since 2021-08-10
 */
enum class ErrorCodes(
    override val code: Int,
    override val defaultMessage: String
) : ErrorCodeContract {
    SERVICE_NOT_FOUND(code = 10, "Requested service is not found."),
    RESOURCE_NOT_FOUND(code = 20, "Requested resource is not found."),
    WRONG_PRESENTATION(code = 30, "Requested data presentation is not supported."),
    WRONG_INPUT(code = 40, "Invalid input value."),
    MALFORMED_INPUT(code = 50, "Malformed input."),

    EXCEPTION_ON_TRANSMISSION(200, "An unhandled exception is occurred on transmission."),

    USER_BY_ID_NOT_FOUND(1001, "User with given id is not found."),
    USER_BY_EMAIL_DUPLICATED(1002, "User with given email is already registered."),
    USER_BY_NICKNAME_DUPLICATED(1003, "User with given nickname is already registered."),

    DATA_INTEGRITY_BROKEN(code = 50000, "Data integrity is broken."),

    UNHANDLED_EXCEPTION(code = -1, "Unhandled internal error. We will thank you for reporting this.");

    companion object {
        @JvmStatic
        fun from(code: Any?): ErrorCodes {
            val convertedCode: Int? = when (code) {
                is Int -> code
                is String -> Integer.decode(code)
                else -> code?.toString()?.toIntOrNull()
            }

            return entries.firstOrNull { it.code == convertedCode } ?: UNHANDLED_EXCEPTION
        }
    }
}
