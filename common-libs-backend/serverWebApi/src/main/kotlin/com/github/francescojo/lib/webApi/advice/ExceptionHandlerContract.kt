/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.webApi.advice

import com.github.francescojo.core.exception.KopringException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus

/**
 * @since 2021-08-10
 */
interface ExceptionHandlerContract<T : Exception> {
    fun onException(req: HttpServletRequest, exception: T): Pair<KopringException, HttpStatus>?
}
