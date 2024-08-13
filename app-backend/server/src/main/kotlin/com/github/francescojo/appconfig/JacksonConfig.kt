/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.appconfig

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.github.francescojo.lib.util.dropFractionalPartIfInteger
import com.github.francescojo.lib.util.truncateToSeconds
import com.github.francescojo.util.WebMvcBindUtils
import io.hypersistence.tsid.TSID
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.annotation.Configuration
import java.math.BigDecimal
import java.time.Instant
import java.time.ZoneOffset.UTC
import java.time.ZonedDateTime
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
            addSerializer(Instant::class.java, JacksonInstantSerializer())
            addDeserializer(Instant::class.java, JacksonInstantDeserializer())

            addSerializer(BigDecimal::class.java, JacksonBigDecimalSerializer())
            addDeserializer(BigDecimal::class.java, JacksonBigDecimalDeserializer())

            addSerializer(TSID::class.java, JacksonTsidSerializer())
            addDeserializer(TSID::class.java, JacksonTsidDeserializer())
        }
        val kotlinModule = KotlinModule.Builder().build()

        defaultObjectMapper.registerModules(JavaTimeModule(), simpleModule, kotlinModule)
    }
}

internal class JacksonInstantSerializer : JsonSerializer<Instant>() {
    override fun serialize(value: Instant?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        gen?.writeString(value?.truncateToSeconds()?.atZone(UTC)?.format(DateTimeFormatter.ISO_DATE_TIME))
    }
}

internal class JacksonInstantDeserializer : JsonDeserializer<Instant>() {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): Instant? {
        return p?.let { ZonedDateTime.parse(it.text, DateTimeFormatter.ISO_DATE_TIME).toInstant().truncateToSeconds() }
    }
}

internal class JacksonBigDecimalSerializer : JsonSerializer<BigDecimal>() {
    override fun serialize(value: BigDecimal?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        gen?.writeString(value?.dropFractionalPartIfInteger()?.toPlainString())
    }
}

internal class JacksonBigDecimalDeserializer : JsonDeserializer<BigDecimal>() {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): BigDecimal? {
        return p?.let { BigDecimal(it.text).dropFractionalPartIfInteger() }
    }
}

internal class JacksonTsidSerializer : JsonSerializer<TSID>(), WebMvcBindUtils {
    override fun serialize(value: TSID?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        gen?.writeString(value?.serialise())
    }
}

internal class JacksonTsidDeserializer : JsonDeserializer<TSID>(), WebMvcBindUtils {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): TSID? {
        return p?.text?.deserialiseToTsid()
    }
}
