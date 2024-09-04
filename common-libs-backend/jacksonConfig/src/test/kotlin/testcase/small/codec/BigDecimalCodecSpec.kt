/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package testcase.small.codec

import com.fasterxml.jackson.core.JsonGenerator
import com.github.francescojo.lib.annotation.SmallTest
import com.github.francescojo.lib.jacksonConfig.serialise.JacksonBigDecimalSerializer
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.ArgumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import java.math.BigDecimal
import java.util.stream.Stream

/**
 * @since 2024-08-20
 */
@SmallTest
internal class BigDecimalCodecSpec {
    private lateinit var serialiser: JacksonBigDecimalSerializer

    @BeforeEach
    fun setUp() {
        serialiser = JacksonBigDecimalSerializer()
    }

    @ParameterizedTest(name = "Trailing zeros in fractional part of BigDecimal is dropped")
    @MethodSource("trailingZerosDroppedArgs")
    fun trailingZerosDropped(
        value: BigDecimal,
        expected: String
    ) {
        // then:
        val result = serialiser.invokeWith(value)

        // expect:
        result shouldBe expected
    }

    @DisplayName("Effective numbers in fractional part is preserved")
    @Test
    fun effectiveNumbersPreserved() {
        // given:
        val value = BigDecimal("123.4567890123456789012345678901")
        val expected = "123.4567890123456789012345678901"

        // then:
        val result = serialiser.invokeWith(value)

        // expect:
        result shouldBe expected
    }

    private fun JacksonBigDecimalSerializer.invokeWith(value: BigDecimal): String {
        val gen = mock<JsonGenerator>()
        this.serialize(value, gen, null)

        val argCaptor = ArgumentCaptor.forClass(String::class.java)
        verify(gen).writeString(argCaptor.capture())

        return argCaptor.value
    }

    companion object {
        @JvmStatic
        fun trailingZerosDroppedArgs(): Stream<Arguments> = Stream.of(
            Arguments.of(BigDecimal("123.456000"), "123.456"),
            Arguments.of(BigDecimal("5.0"), "5")
        )
    }
}
