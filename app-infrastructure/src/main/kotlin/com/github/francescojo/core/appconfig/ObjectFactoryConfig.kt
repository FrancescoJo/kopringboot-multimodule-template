/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.appconfig

import com.github.francescojo.core.domain.user.UserObjectFactory
import com.github.francescojo.core.jdbc.user.UserObjectFactoryImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @since 2021-08-10
 */
@Configuration
class ObjectFactoryConfig {
    @Bean
    fun userObjectFactory(): UserObjectFactory = UserObjectFactoryImpl
}
