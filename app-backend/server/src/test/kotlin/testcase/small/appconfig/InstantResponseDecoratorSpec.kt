/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package testcase.small.appconfig

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.github.francescojo.appconfig.InstantResponseDecorator
import com.github.francescojo.lib.annotation.SmallTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.ArgumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import java.time.Instant
import java.util.stream.Stream

/**
 * @since 2021-08-10
 */
@SmallTest
class InstantResponseDecoratorSpec {
    private lateinit var sut: InstantResponseDecorator
    private lateinit var mockGen: JsonGenerator
    private lateinit var serializers: SerializerProvider

    @BeforeEach
    fun setUp() {
        mockGen = mock()
        serializers = mock()
        sut = InstantResponseDecorator()
    }

    @ParameterizedTest(name = "Zero values should never be truncated")
    @MethodSource("timeValues")
    fun zeroValuesMustBePreserved(
        value: Instant,
        expected: String
    ) {
        // when:
        sut.serialize(value, mockGen, serializers)

        // then:
        val argCaptor = ArgumentCaptor.forClass(String::class.java)
        verify(mockGen).writeString(argCaptor.capture())

        // expect:
        assertThat(argCaptor.value, `is`(expected))
    }

    companion object {
        @JvmStatic
        fun timeValues(): Stream<Arguments> = Stream.of(
            Arguments.of(Instant.parse("2022-01-01T00:00:00Z"), "2022-01-01T00:00:00Z"),
            Arguments.of(Instant.parse("2022-01-01T09:00:00+09:00"), "2022-01-01T00:00:00Z")
        )
    }
}
