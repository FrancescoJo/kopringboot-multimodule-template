/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package testcase.large

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.francescojo.KopringApplication
import com.github.francescojo.endpoint.ErrorResponseEnvelope
import com.github.francescojo.lib.annotation.LargeTest
import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.config.ObjectMapperConfig
import io.restassured.config.RestAssuredConfig
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInfo
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation
import org.springframework.restdocs.snippet.Snippet
import org.springframework.test.context.ActiveProfiles
import test.endpoint.v1.JsonRequestAssertionsMixin
import java.util.ArrayList
import java.util.HashMap
import java.util.concurrent.atomic.AtomicInteger
import kotlin.reflect.KClass

/**
 * @since 2021-08-10
 */
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [
        KopringApplication::class,
    ]
)
@ExtendWith(RestDocumentationExtension::class)
@ActiveProfiles("largeTest")
@LargeTest
class RestAssuredLargeTestBase : JsonRequestAssertionsMixin {
    val applicationContext: ApplicationContext
        get() {
            if (::_applicationContext.isInitialized) {
                return _applicationContext
            } else {
                throw IllegalStateException(
                    "No Test ApplicationContext is injected. Is SpringBootTest is running properly?"
                )
            }
        }

    private val log = LoggerFactory.getLogger(RestAssuredLargeTestBase::class.java)

    @Autowired
    override lateinit var defaultObjMapper: ObjectMapper

    @Autowired
    private lateinit var _applicationContext: ApplicationContext

    @Value("\${local.server.port}")
    private var port: Int = DOCUMENTATION_DEFAULT_PORT

    private lateinit var testInfo: TestInfo
    private lateinit var restDocumentation: RestDocumentationContextProvider

    @BeforeEach
    fun setupTestBase(testInfo: TestInfo, restDocumentation: RestDocumentationContextProvider) {
        this.testInfo = testInfo
        this.restDocumentation = restDocumentation
        RestAssured.config = RestAssuredConfig.config().objectMapperConfig(
            ObjectMapperConfig().jackson2ObjectMapperFactory { _, _ -> defaultObjMapper }
        )
    }

    /**
     * This extension function creates RESTAssured documentation by Test executing class and method names.
     *
     * The following precautions should be considered:
     *
     * 1. It is encouraged to name Classes and Methods with alphanumeric characters only.
     *    Although kotlin allows special characters as symbol names if wrapped in backticks, since this method
     *    creates paths on the local filesystem by its names, therefore tests might be failed if your names include
     *    reserved/restricted letters by operating system.
     *
     * 2. Numbers are automatically postfixed if this method is executed in parameterized test, which honours
     *    the test execution sequences. For example, the following test code would:
     *    ```
     *    @ParameterizedTest
     *    @ValueSource(strings = ["a", "b", "c"])
     *    fun stringInput(value: String)
     *    ```
     *    create `stringInput#1`, `stringInput#2`, `stringInput#3` API documentations, respectively.
     */
    fun RequestSpecification.withDocumentation(
        prefix: String? = null,
        reqFields: List<FieldDescriptor>? = null,
        respFields: List<FieldDescriptor>? = null,
    ): RequestSpecification {
        if (reqFields == null && respFields == null) {
            with(testInfo) {
                val testSuite = testClass.get().canonicalName
                val testName = testMethod.get().name

                log.warn("Skipping document creation for: {}#{}", testSuite, testName)
            }

            return this
        }

        // Dangerous operation - we must have canonical class names and test method names in JUnit tests
        val (defaultDocumentId, isParameterised) = with(testInfo) {
            val testGroup = testClass.get().run {
                val baseName = if (packageName.isEmpty()) {
                    canonicalName
                } else {
                    canonicalName.substring(packageName.length + 1)
                }

                return@run if (prefix.isNullOrEmpty()) {
                    baseName
                } else {
                    "${prefix}/${baseName}"
                }
            }.replace(".", "/")
            val method = testMethod.get()
            val testName = method.name

            return@with "${testGroup}/${testName}" to (method.getAnnotation(ParameterizedTest::class.java) != null)
        }

        // Automatic numbering for parameterized tests
        val documentId = if (isParameterised) {
            val index = TEST_INDICES[defaultDocumentId] ?: run {
                val initialIndex = AtomicInteger(1)
                TEST_INDICES[defaultDocumentId] = initialIndex
                return@run initialIndex
            }

            "${defaultDocumentId}#${index.getAndIncrement()}"
        } else {
            defaultDocumentId
        }

        return this.filter(
            RestAssuredRestDocumentation.document(
                documentId,
                Preprocessors.preprocessRequest(
                    Preprocessors.prettyPrint(),
                    Preprocessors.modifyUris().host(DOCUMENTATION_DEFAULT_HOST).removePort()
                ),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                *(ArrayList<Snippet>().apply {
                    reqFields?.takeIf { it.isNotEmpty() }?.let { add(PayloadDocumentation.requestFields(it)) }
                    respFields?.takeIf { it.isNotEmpty() }?.let { add(PayloadDocumentation.responseFields(it)) }
                }.toTypedArray())
            )
        )
    }

    fun request(userAgent: String? = null): RequestSpecification = RestAssured.given(
        RequestSpecBuilder()
            .addFilter(RestAssuredRestDocumentation.documentationConfiguration(restDocumentation))
            .build()
            .log().all()
            .port(port)
            .contentType(MediaType.APPLICATION_JSON.toString())
            .accept(MediaType.APPLICATION_JSON.toString())
    )

    fun <T : Any> Response.expect2xx(
        responseType: KClass<T>,
        httpStatus: HttpStatus = HttpStatus.OK,
    ): T {
        val parsed = this.toJsonStringWith(httpStatus)

        return parse2xxResponse(parsed, responseType)
    }

    fun Response.expect4xx(httpStatus: HttpStatus = HttpStatus.BAD_REQUEST): ErrorResponseEnvelope.Body {
        val parsed = this.toJsonStringWith(httpStatus)

        return parse4xxResponse(parsed)
    }

    private fun Response.toJsonStringWith(httpStatus: HttpStatus) =
        this.then().assertThat().statusCode(`is`(httpStatus.value())).extract().body().asString()

    companion object {
        private const val DOCUMENTATION_DEFAULT_HOST = "localhost"
        private const val DOCUMENTATION_DEFAULT_PORT = 8080
        private val TEST_INDICES = HashMap<String, AtomicInteger>()
    }
}
