/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package test.endpoint.v1

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.github.francescojo.core.exception.ErrorCodes
import com.github.francescojo.endpoint.ErrorResponseEnvelope
import com.github.francescojo.endpoint.ResponseEnvelope
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Instant
import kotlin.reflect.KClass

/**
 * @since 2021-08-10
 */
interface JsonRequestAssertionsMixin {
    val defaultObjMapper: ObjectMapper

    private val log: Logger
        get() = LoggerFactory.getLogger(this::class.java)

    fun ErrorResponseEnvelope.Body.withExceptionCode(expectedCode: ErrorCodes) {
        val actual = ErrorCodes.from(this.code)

        if (actual != expectedCode) {
            throw AssertionError("Expected error code: $expectedCode, but was: $actual")
        }
    }

    fun <T : Any> parse2xxResponse(responseStr: String, responseType: KClass<T>): T {
        val parsed = parseResponse(responseStr).also { it.assertResponseTypeIs(ResponseEnvelope.Type.OK) }
        return defaultObjMapper.convertValue(parsed.body, responseType.java)
    }

    fun parse4xxResponse(responseStr: String): ErrorResponseEnvelope.Body {
        val parsed = parseResponse(responseStr).also { it.assertResponseTypeIs(ResponseEnvelope.Type.ERROR) }
        return defaultObjMapper.convertValue(parsed.body, ErrorResponseEnvelope.Body::class.java)
    }

    private fun parseResponse(responseStr: String): ResponseEnvelope<Map<String, *>> {
        try {
            @Suppress("UNCHECKED_CAST")     // As JSON contract: <String, Any>
            val rawResponseMap = defaultObjMapper.readValue(responseStr, MutableMap::class.java) as Map<String, *>

            val strType = rawResponseMap["type"] as String

            @Suppress("UNCHECKED_CAST")     // As JSON contract: <String, Any>
            val jsonBody = rawResponseMap["body"] as Map<String, *>
            val timestamp = Instant.parse(rawResponseMap["timestamp"] as String)

            return ResponseEnvelopeImpl(
                type = ResponseEnvelope.Type.from(strType),
                timestamp = timestamp,
                body = jsonBody
            )
        } catch (e: Exception) {
            log.error("responseStr = $responseStr")
            throw e
        }
    }

    private fun ResponseEnvelope<*>.assertResponseTypeIs(type: ResponseEnvelope.Type) {
        if (this.type !== type) {
            this.printAsError()
            throw AssertionError("Not a '$type' type response")
        }
    }

    private fun ResponseEnvelope<*>.printAsError() {
        log.error(defaultObjMapper.copy().enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(this))
    }

    private data class ResponseEnvelopeImpl<T>(
        override val type: Type,
        override val timestamp: Instant,
        override val body: T?
    ) : ResponseEnvelope<T>(type, timestamp)
}
