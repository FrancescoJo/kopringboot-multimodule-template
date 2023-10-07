/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.util

import jakarta.servlet.RequestDispatcher
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus

fun HttpServletRequest.toHttpStatus(): HttpStatus {
    (getAttribute(RequestDispatcher.ERROR_STATUS_CODE) as? Int)?.let {
        return HttpStatus.valueOf(it)
    }

    return HttpStatus.INTERNAL_SERVER_ERROR
}

fun HttpServletRequest.originalRequestUri(): String =
    (getAttribute(RequestDispatcher.ERROR_REQUEST_URI) as? String) ?: ""
