/*
 * kopringboot-multimodule-template
 * Distributed under CC BY-NC-SA
 */
package com.github.francescojo.util

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

/**
 * a `null` and exception safe [HttpStatusCode] to [HttpStatus] conversion logic,
 * to provide an easy conversion between both types, which was possible before Spring framework 6.
 */
fun HttpStatusCode.toHttpStatus(): HttpStatus =
    HttpStatus.resolve(this.value()) ?: HttpStatus.INTERNAL_SERVER_ERROR
