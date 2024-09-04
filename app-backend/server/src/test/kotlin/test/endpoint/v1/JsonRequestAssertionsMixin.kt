/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package test.endpoint.v1

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.francescojo.core.exception.ErrorCodes
import com.github.francescojo.lib.webApi.response.base.ErrorResponseEnvelope
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import test.endpoint.JsonStringResponseParser
import test.endpoint.ResponseEnvelopeAssertions
import kotlin.reflect.KClass

/**
 * @since 2021-08-10
 */
interface JsonRequestAssertionsMixin {
    val defaultObjMapper: ObjectMapper

    private val log: Logger
        get() = LoggerFactory.getLogger(this::class.java)

    fun ErrorResponseEnvelope.Body.withExceptionCode(expectedCode: ErrorCodes) =
        ResponseEnvelopeAssertions.shouldHaveErrorCode(this, expectedCode)

    fun <T : Any> parse2xxResponse(responseStr: String, responseType: KClass<T>): T =
        JsonStringResponseParser.parse2xxResponse(defaultObjMapper, responseStr, responseType, log)

    fun parse4xxResponse(responseStr: String): ErrorResponseEnvelope.Body =
        JsonStringResponseParser.parse4xxResponse(defaultObjMapper, responseStr, log)
}
