/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package testcase.small.codec

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.github.francescojo.lib.annotation.SmallTest
import com.github.francescojo.lib.jacksonConfig.deserialise.JacksonInstantDeserializer
import com.github.francescojo.lib.jacksonConfig.serialise.JacksonInstantSerializer
import com.github.francescojo.lib.time.truncateToSeconds
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import java.time.Instant
import java.util.stream.Stream

/**
 * @since 2021-08-10
 */
@SmallTest
internal class InstantCodecSpec {
    private lateinit var serialiser: JacksonInstantSerializer
    private lateinit var deserialiser: JacksonInstantDeserializer

    @BeforeEach
    fun setUp() {
        serialiser = JacksonInstantSerializer()
        deserialiser = JacksonInstantDeserializer()
    }

    @ParameterizedTest(name = "Zero values should never be truncated")
    @MethodSource("timeValues")
    fun zeroValuesMustBePreserved(
        value: Instant,
        expected: String
    ) {
        // then:
        val result = serialiser.invokeWith(value)

        // expect:
        result shouldBe expected
    }

    @DisplayName("No unexpected data loss between serialisation and deserialisation")
    @Test
    fun deserialisedAsGiven() {
        // given:
        val now = Instant.now()

        // when:
        val serialised = serialiser.invokeWith(now)

        // then:
        val deserialised = deserialiser.invokeWith(serialised)

        // expect:
        deserialised shouldBe now.truncateToSeconds()
    }

    private fun JacksonInstantSerializer.invokeWith(value: Instant): String {
        val gen = mock<JsonGenerator>()
        this.serialize(value, gen, null)

        val argCaptor = ArgumentCaptor.forClass(String::class.java)
        verify(gen).writeString(argCaptor.capture())

        return argCaptor.value
    }

    private fun JacksonInstantDeserializer.invokeWith(value: String): Instant? {
        val parser = mock<JsonParser>()
        `when`(parser.text).thenReturn(value)

        return this.deserialize(parser, null)
    }

    companion object {
        @JvmStatic
        fun timeValues(): Stream<Arguments> = Stream.of(
            Arguments.of(Instant.parse("2022-01-01T00:00:00Z"), "2022-01-01T00:00:00Z"),
            Arguments.of(Instant.parse("2022-01-01T09:00:00+09:00"), "2022-01-01T00:00:00Z")
        )
    }
}
