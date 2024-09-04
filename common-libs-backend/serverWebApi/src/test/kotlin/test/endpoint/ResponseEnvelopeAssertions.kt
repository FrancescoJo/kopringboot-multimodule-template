/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package test.endpoint

import com.github.francescojo.core.exception.ErrorCodes
import com.github.francescojo.lib.webApi.response.base.ErrorResponseEnvelope

/**
 * @since 2024-09-04
 */
object ResponseEnvelopeAssertions {
    fun shouldHaveErrorCode(
        actual: ErrorResponseEnvelope.Body,
        expectedCode: ErrorCodes
    ) {
        val actualCode = ErrorCodes.from(actual.code)

        if (actualCode != expectedCode) {
            throw AssertionError("Expected error code: $expectedCode, but was: $actualCode")
        }
    }
}
