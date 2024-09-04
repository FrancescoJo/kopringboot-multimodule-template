/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package test.endpoint

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.francescojo.lib.webApi.response.base.ErrorResponseEnvelope
import io.restassured.response.Response
import org.slf4j.Logger
import org.springframework.http.HttpStatus
import spock.lang.Specification

/**
 * @since 2024-09-04
 */
abstract class AbstractRestAssuredTestBase extends Specification {
    protected abstract Logger getLog();

    protected abstract ObjectMapper getObjectMapper();

    final <T> OnGoingRestAssuredResponseAssertion<T> expect2xx(
            final Response response,
            final Class<T> responseType,
            final HttpStatus httpStatus = HttpStatus.OK
    ) {
        final jsonString = parseAsJsonString(response, httpStatus)
        final parsedObject =
                JsonStringResponseParser.INSTANCE.parse2xxResponse(objectMapper, jsonString, responseType, log)

        return new OnGoingRestAssuredResponseAssertion(response, parsedObject)
    }

    final OnGoingRestAssuredResponseAssertion<ErrorResponseEnvelope> expect4xx(
            final Response response,
            final HttpStatus httpStatus = HttpStatus.BAD_REQUEST
    ) {
        final jsonString = parseAsJsonString(response, httpStatus)
        final parsedObject =
                JsonStringResponseParser.INSTANCE.parse4xxResponse(objectMapper, jsonString, log)

        return new OnGoingRestAssuredResponseAssertion(response, parsedObject)
    }

    private String parseAsJsonString(
            final Response response,
            final HttpStatus httpStatus
    ) {
        if (response.statusCode() != httpStatus.value()) {
            final errorMsg = "Expected HTTP status code ${httpStatus.value()} but got ${response.statusCode()}"
            log.error(errorMsg)
            log.error("Response body: \n{}", response.body().asPrettyString())

            throw new AssertionError(errorMsg as Object)
        }

        return response.then().extract().body().asString()
    }
}
