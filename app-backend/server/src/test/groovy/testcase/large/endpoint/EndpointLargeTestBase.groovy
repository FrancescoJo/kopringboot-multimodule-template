/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package testcase.large.endpoint

import testcase.large.RestAssuredLargeTestBase

/**
 * @since 2024-09-03
 */
class EndpointLargeTestBase extends RestAssuredLargeTestBase {
    String uriToRelativePath(final URI self) {
        final it = self.toString()

        if (it.startsWith("/")) {
            if (it.length() == 1) {
                return ""
            } else {
                return it.substring(1)
            }
        } else {
            return it
        }
    }
}
