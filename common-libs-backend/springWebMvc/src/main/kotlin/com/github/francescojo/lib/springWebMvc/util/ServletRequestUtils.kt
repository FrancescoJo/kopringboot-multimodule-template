/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.springWebMvc.util

import com.github.francescojo.lib.util.isNullOrUnicodeBlank
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

/**
 * This logic searches for "X-Real-IP" header first and will use the value if query was hit,
 * otherwise try on "X-forwarded-for" header as callback and use the value if found,
 * and will use [HttpServletRequest.getRemoteAddr] for last resort which is not accurate under
 * reverse proxy configuration.
 *
 * Note that this approach is not accurate on various web containers and additional logic may be
 * needed to fulfill your business requirement.
 */
@Suppress("ReturnCount")    // Early return is much readable in this case
fun HttpServletRequest.extractIpStr(): String {
    with(getHeader("X-Real-IP")) {
        if (!isNullOrUnicodeBlank() && !isUnknown()) {
            return this
        }
    }

    with(getHeader("Proxy-Client-IP")) {
        if (!isNullOrUnicodeBlank() && !isUnknown()) {
            return this
        }
    }

    with(getHeader("X-forwarded-for")) {
        if (isNullOrUnicodeBlank()) {
            return@with
        }

        split(",").let {
            if (!it[0].isUnknown()) {
                return it[0]
            }
        }
    }

    return remoteAddr
}

fun HttpServletRequest.userAgent(): String = getHeader("User-Agent") ?: ""

private fun String.isUnknown() = "unknown".equals(this, true)
