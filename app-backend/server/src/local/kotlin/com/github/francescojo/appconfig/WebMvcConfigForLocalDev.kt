/*
 * kopringboot-multimodule-template
 * Sir.LOIN Intellectual property. All rights reserved.
 */
package com.github.francescojo.appconfig

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.CommonsRequestLoggingFilter

/**
 * @since 2024-08-05
 */
@Configuration
class WebMvcConfigForLocalDev {
    @Bean
    fun logFilter() = CommonsRequestLoggingFilter().apply {
        setIncludeQueryString(true)
        setIncludePayload(true)
        setMaxPayloadLength(LOG_PAYLOAD_LENGTH)
        setIncludeHeaders(true)
        setAfterMessagePrefix("REQUEST DATA : ")
    }

    companion object {
        private const val LOG_PAYLOAD_LENGTH = 4096
    }
}
