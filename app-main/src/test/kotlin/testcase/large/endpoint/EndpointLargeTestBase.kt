/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package testcase.large.endpoint

import testcase.large.RestAssuredLargeTestBase
import java.net.URI

/**
 * @since 2021-08-10
 */
class EndpointLargeTestBase : RestAssuredLargeTestBase() {
    fun URI.toRelativePath(): String {
        return toString().let {
            if (it.startsWith("/")) {
                if (it.length == 1) {
                    ""
                } else {
                    it.substring(1)
                }
            } else {
                it
            }
        }
    }
}
