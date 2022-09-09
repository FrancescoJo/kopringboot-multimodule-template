/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.advice.errorhandler

import com.github.francescojo.core.exception.KopringException
import org.springframework.http.HttpStatus
import javax.servlet.http.HttpServletRequest

/**
 * @since 2021-08-10
 */
interface ExceptionHandlerContract<T : Exception> {
    fun onException(req: HttpServletRequest, exception: T): Pair<KopringException, HttpStatus>?
}
