/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.appconfig

import com.github.francescojo.advice.AcceptLanguageLocaleProvider
import com.github.francescojo.core.i18n.LocaleProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.http.HttpHeaders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CommonsRequestLoggingFilter
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.servlet.http.HttpServletRequest

/**
 * @since 2021-08-10
 */
@Configuration
class WebMvcConfig : WebMvcConfigurer {
    @Bean
    @Scope(WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    fun acceptLanguageLocaleProvider(
        request: HttpServletRequest
    ): LocaleProvider = AcceptLanguageLocaleProvider(request.getHeader(HttpHeaders.ACCEPT_LANGUAGE))

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
