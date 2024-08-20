/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.appconfig

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.francescojo.lib.jacksonConfig.JacksonConfigHelper
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order

/**
 * @since 2021-08-10
 */
@Order(Ordered.LOWEST_PRECEDENCE)
@Configuration
class JacksonConfig(
    private val defaultObjectMapper: ObjectMapper
) : InitializingBean {
    override fun afterPropertiesSet() {
        with(JacksonConfigHelper) { defaultObjectMapper.addCommonConfig() }
    }
}
