/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package testcase.small.codec

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.github.francescojo.lib.annotation.SmallTest
import com.github.francescojo.lib.jacksonConfig.deserialise.JacksonTsidDeserializer
import com.github.francescojo.lib.jacksonConfig.serialise.JacksonTsidSerializer
import io.hypersistence.tsid.TSID
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

/**
 * @since 2024-08-20
 */
@SmallTest
internal class TsidCodecSpec {
    private lateinit var serialiser: JacksonTsidSerializer
    private lateinit var deserialiser: JacksonTsidDeserializer

    @BeforeEach
    fun setUp() {
        serialiser = JacksonTsidSerializer()
        deserialiser = JacksonTsidDeserializer()
    }

    @DisplayName("Original value must be preserved during serialisation and deserialisation")
    @Test
    fun valueMustBePreserved() {
        // given:
        val value = TSID.fast()

        // when:
        val serialised = serialiser.invokeWith(value)

        // then:
        val deserialised = deserialiser.invokeWith(serialised)

        // expect:
        deserialised shouldBe value
    }

    private fun JacksonTsidSerializer.invokeWith(value: TSID): String {
        val gen = mock<JsonGenerator>()
        this.serialize(value, gen, null)

        val argCaptor = ArgumentCaptor.forClass(String::class.java)
        verify(gen).writeString(argCaptor.capture())

        return argCaptor.value
    }

    private fun JacksonTsidDeserializer.invokeWith(value: String): TSID? {
        val parser = mock<JsonParser>()
        `when`(parser.text).thenReturn(value)

        return this.deserialize(parser, null)
    }
}
