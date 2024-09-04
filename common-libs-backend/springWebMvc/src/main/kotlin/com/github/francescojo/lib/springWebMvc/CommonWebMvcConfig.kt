/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.springWebMvc

import com.github.francescojo.lib.i18n.LocaleProvider
import com.github.francescojo.lib.springWebMvc.advice.AcceptLanguageLocaleProvider
import com.github.francescojo.lib.springWebMvc.converter.StringToTsidConverter
import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.format.FormatterRegistry
import org.springframework.http.HttpHeaders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * @since 2021-08-10
 */
@Configuration
class CommonWebMvcConfig : WebMvcConfigurer {
    @Bean
    @Scope(WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    fun acceptLanguageLocaleProvider(
        request: HttpServletRequest
    ): LocaleProvider = AcceptLanguageLocaleProvider(request.getHeader(HttpHeaders.ACCEPT_LANGUAGE))

    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverter(StringToTsidConverter())
    }
}
