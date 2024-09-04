/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package test.endpoint

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.github.francescojo.lib.webApi.response.base.ErrorResponseEnvelope
import com.github.francescojo.lib.webApi.response.base.ResponseEnvelope
import org.slf4j.Logger
import java.time.Instant
import kotlin.reflect.KClass

/**
 * @since 2024-09-04
 */
object JsonStringResponseParser {
    fun <T : Any> parse2xxResponse(
        objectMapper: ObjectMapper,
        responseStr: String,
        responseType: KClass<T>,
        log: Logger? = null
    ): T = parse2xxResponse(objectMapper, responseStr, responseType.java, log)

    fun <T : Any> parse2xxResponse(
        objectMapper: ObjectMapper,
        responseStr: String,
        responseType: Class<T>,
        log: Logger? = null
    ): T =
        convertToResponseEnvelope(objectMapper, responseStr, log)
            .parseTo(responseType, objectMapper, ResponseEnvelope.Type.OK, log)

    fun parse4xxResponse(
        objectMapper: ObjectMapper,
        responseStr: String,
        log: Logger? = null
    ): ErrorResponseEnvelope.Body =
        convertToResponseEnvelope(objectMapper, responseStr, log)
            .parseTo(ErrorResponseEnvelope.Body::class.java, objectMapper, ResponseEnvelope.Type.ERROR, log)

    private fun convertToResponseEnvelope(
        objectMapper: ObjectMapper,
        responseStr: String,
        log: Logger? = null
    ): ResponseEnvelope<Map<String, *>> {
        try {
            @Suppress("UNCHECKED_CAST")     // As JSON contract: <String, Any>
            val rawResponseMap = objectMapper.readValue(responseStr, MutableMap::class.java) as Map<String, *>

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
            log?.error("responseStr = $responseStr")
            throw e
        }
    }

    private fun <T : Any> ResponseEnvelope<Map<String, *>>.parseTo(
        responseType: Class<T>,
        objectMapper: ObjectMapper,
        type: ResponseEnvelope.Type,
        log: Logger?
    ): T {
        if (this.type !== type) {
            log?.error(objectMapper.copy().enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(this))
            throw AssertionError("Not a '$type' type response")
        }

        return objectMapper.convertValue(body, responseType)
    }

    private data class ResponseEnvelopeImpl<T>(
        override val type: Type,
        override val timestamp: Instant,
        override val body: T?
    ) : ResponseEnvelope<T>(type, timestamp)
}
