/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.appconfig

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.annotation.Configuration
import java.time.Instant
import java.time.ZoneOffset.UTC
import java.time.format.DateTimeFormatter

/**
 * @since 2021-08-10
 */
@Configuration
class JacksonConfig(
    private val defaultObjectMapper: ObjectMapper
) : InitializingBean {
    override fun afterPropertiesSet() {
        val simpleModule = SimpleModule().apply {
            addSerializer(Instant::class.java, InstantResponseDecorator())
        }
        val kotlinModule = KotlinModule.Builder().build()

        defaultObjectMapper.registerModules(simpleModule, kotlinModule)
    }
}

internal class InstantResponseDecorator : JsonSerializer<Instant>() {
    override fun serialize(value: Instant?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        gen?.writeString(value?.atZone(UTC)?.format(DateTimeFormatter.ISO_DATE_TIME))
    }
}
