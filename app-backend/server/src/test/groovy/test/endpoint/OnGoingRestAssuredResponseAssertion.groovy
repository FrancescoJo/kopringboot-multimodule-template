/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package test.endpoint

import com.github.francescojo.core.exception.ErrorCodes
import com.github.francescojo.lib.webApi.response.base.ErrorResponseEnvelope
import io.restassured.response.Response

/**
 * Since declaring extension functions in Groovy is quite cumbersome in contrast to Kotlin,
 * we will use this class to support various assertions on {@link io.restassured.response.Response}
 * in extension function form.
 *
 * <p>
 * Code example on 2xx response assertion:
 * <pre>
 * when:
 * final Response response = someApiCall(arg1, arg2, ...)
 *
 * then:
 * expect2xx(response)
 *     .containsHeader("Content-Type", "application/json")
 * </pre>
 * </p>
 *
 * <p>
 * Code example on 4xx or 5xx response assertion:
 * <pre>
 * when:
 * final Response response = someApiCall(arg1, arg2, ...)
 *
 * then:
 * expect4xx(response)
 *     .containsHeader("Content-Type", "application/json")
 *     .withExceptionCode(ErrorCodes.MALFORMED_INPUT)
 * </pre>
 * </p>
 *
 * @since 2024-09-04
 */
class OnGoingRestAssuredResponseAssertion<T> {
    private final Response response;
    private final T object

    OnGoingRestAssuredResponseAssertion(
            final Response response,
            final T object
    ) {
        this.response = response
        this.object = object
    }

    OnGoingRestAssuredResponseAssertion<T> withExceptionCode(final ErrorCodes expectedCode) {
        if (!(this.object instanceof ErrorResponseEnvelope.Body)) {
            throw new AssertionError(
                    ("This assertion is only applicable for ErrorResponseEnvelope.Body type. " +
                            "Actual type: ${this.object.getClass().getSimpleName()}") as Object
            )
        }

        final errorResponseBody = this.object as ErrorResponseEnvelope.Body
        final actual = ErrorCodes.from(errorResponseBody.code)

        if (actual != expectedCode) {
            throw new AssertionError("Expected error code: $expectedCode, but was: $actual" as Object)
        }

        return this
    }

    T get() {
        return object
    }
}
