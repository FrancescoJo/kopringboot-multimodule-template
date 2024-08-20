/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.jacksonConfig

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.github.francescojo.lib.jacksonConfig.deserialise.JacksonBigDecimalDeserializer
import com.github.francescojo.lib.jacksonConfig.deserialise.JacksonInstantDeserializer
import com.github.francescojo.lib.jacksonConfig.deserialise.JacksonTsidDeserializer
import com.github.francescojo.lib.jacksonConfig.serialise.JacksonBigDecimalSerializer
import com.github.francescojo.lib.jacksonConfig.serialise.JacksonInstantSerializer
import com.github.francescojo.lib.jacksonConfig.serialise.JacksonTsidSerializer
import io.hypersistence.tsid.TSID
import java.math.BigDecimal
import java.time.Instant

/**
 * @since 2024-08-19
 */
object JacksonConfigHelper {
    private const val MODULE_ID = "com.bondaero.common.jacksonConfig"

    fun ObjectMapper.addCommonConfig() {
        if (this.registeredModuleIds.contains(MODULE_ID)) {
            return
        }

        val simpleModule = SimpleModule(MODULE_ID).apply {
            addSerializer(Instant::class.java, JacksonInstantSerializer())
            addDeserializer(Instant::class.java, JacksonInstantDeserializer())

            addSerializer(BigDecimal::class.java, JacksonBigDecimalSerializer())
            addDeserializer(BigDecimal::class.java, JacksonBigDecimalDeserializer())

            addSerializer(TSID::class.java, JacksonTsidSerializer())
            addDeserializer(TSID::class.java, JacksonTsidDeserializer())
        }

        /*
         * IMPORTANT: Since JavaTimeModule also registers serializers for Instant,
         * module order is very important to ensure our custom serializers are used.
         */
        registerModules(JavaTimeModule(), simpleModule, KotlinModule.Builder().build())
    }
}
