/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package testcase.large

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.francescojo.KopringApplicationBootstrap
import com.github.francescojo.lib.annotation.LargeTest
import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.test.context.ActiveProfiles
import test.TestContainerMariaDBConfig
import test.endpoint.AbstractRestAssuredTestBase

/**
 * @since 2021-08-10
 */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [
                KopringApplicationBootstrap.class,
                TestContainerMariaDBConfig.class
        ]
)
@ExtendWith(RestDocumentationExtension.class)
@ActiveProfiles("largeTest")
@LargeTest
class RestAssuredLargeTestBase extends AbstractRestAssuredTestBase {
    private static final String DOCUMENTATION_DEFAULT_HOST = "localhost"
    private static final int DOCUMENTATION_DEFAULT_PORT = 8080

    static {
        /**
         * This configuration forces Test Containers to start before any test execution.
         */
        TestContainerMariaDBConfig.forceStartContainer()
    }

    @Autowired
    private ApplicationContext _applicationContext

    @Autowired
    private ObjectMapper defaultObjMapper

    @Value("\${local.server.port}")
    private int port = DOCUMENTATION_DEFAULT_PORT

    @Override
    Logger getLog() {
        return LoggerFactory.getLogger(RestAssuredLargeTestBase.class)
    }

    @Override
    ObjectMapper getObjectMapper() {
        return defaultObjMapper
    }

    final RequestSpecification request(final String userAgent = null) {
        final b = new RequestSpecBuilder()
        // .addFilter(RestAssuredRestDocumentation.documentationConfiguration(restDocumentation))
        if (userAgent != null) {
            b.addHeader("User-Agent", userAgent)
        }

        return RestAssured.given(
                b.build()
                        .log().all()
                        .port(port)
                        .contentType(MediaType.APPLICATION_JSON.toString())
                        .accept(MediaType.APPLICATION_JSON.toString())
        )
    }

    protected final ApplicationContext getApplicationContext() {
        if (_applicationContext == null) {
            throw new IllegalStateException(
                    "No Test ApplicationContext is injected. Is SpringBootTest running properly?"
            )
        } else {
            return _applicationContext
        }
    }

}
