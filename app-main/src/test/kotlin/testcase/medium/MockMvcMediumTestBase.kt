/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package testcase.medium

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.github.francescojo.KopringApplication
import com.github.francescojo.appconfig.JacksonConfig
import com.github.francescojo.appconfig.WebMvcConfig
import com.github.francescojo.core.CoreKopringApplication
import com.github.francescojo.core.appconfig.LoggerConfig
import com.github.francescojo.endpoint.ErrorResponseEnvelope
import com.github.francescojo.lib.annotation.MediumTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.ComponentScan
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import test.endpoint.v1.JsonRequestAssertionsMixin
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * @since 2021-08-10
 */
@WebMvcTest(
    properties = [
        "security.basic.enabled=false"
    ]
)
@ContextConfiguration(
    classes = [
        LoggerConfig::class,
        WebMvcConfig::class,
        JacksonConfig::class
    ],
)
@ComponentScan(
    basePackages = [
        CoreKopringApplication.PACKAGE_NAME,
        "${KopringApplication.PACKAGE_NAME}.advice"
    ]
)
@AutoConfigureMockMvc(addFilters = false)
@WebAppConfiguration
@ActiveProfiles("mediumTest")
@MediumTest
class MockMvcMediumTestBase : JsonRequestAssertionsMixin {
    @MockBean
    private lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    override lateinit var defaultObjMapper: ObjectMapper

    // region request sender utility methods
    protected fun get(
        endpoint: String,
        vararg queryParams: Pair<String, String?>
    ): MockHttpServletRequestBuilder =
        request(HttpMethod.GET, endpoint, null, *queryParams)

    protected fun post(
        endpoint: String,
        payload: Any? = null,
        vararg queryParams: Pair<String, String?>
    ): MockHttpServletRequestBuilder =
        request(HttpMethod.POST, endpoint, payload, *queryParams)

    protected fun patch(
        endpoint: String,
        payload: Any? = null,
        vararg queryParams: Pair<String, String?>
    ): MockHttpServletRequestBuilder =
        request(HttpMethod.PATCH, endpoint, payload, *queryParams)

    protected fun put(
        endpoint: String,
        payload: Any? = null,
        vararg queryParams: Pair<String, String?>
    ): MockHttpServletRequestBuilder =
        request(HttpMethod.PUT, endpoint, payload, *queryParams)

    protected fun delete(
        endpoint: String,
        vararg queryParams: Pair<String, String?>
    ): MockHttpServletRequestBuilder =
        request(HttpMethod.DELETE, endpoint, null, *queryParams)

    protected fun request(
        method: HttpMethod,
        endpoint: String,
        payload: Any? = null,
        vararg queryParams: Pair<String, String?>
    ): MockHttpServletRequestBuilder {
        fun String.urlEncode() = URLEncoder.encode(this, StandardCharsets.UTF_8.toString())
        val requestEndpoint = if (queryParams.isEmpty()) {
            endpoint
        } else {
            "$endpoint?" + queryParams.joinToString("&") {
                it.first.urlEncode() + "=" + (it.second?.urlEncode() ?: "")
            }
        }

        val requestBuilder = when (method) {
            HttpMethod.GET -> MockMvcRequestBuilders.get(requestEndpoint)
            HttpMethod.POST -> MockMvcRequestBuilders.post(requestEndpoint)
            HttpMethod.PATCH -> MockMvcRequestBuilders.patch(requestEndpoint)
            HttpMethod.PUT -> MockMvcRequestBuilders.put(requestEndpoint)
            HttpMethod.DELETE -> MockMvcRequestBuilders.delete(requestEndpoint)
            else -> throw UnsupportedOperationException("$method is not supported.")
        }

        return if (payload == null) {
            requestBuilder.accept(MediaType.APPLICATION_JSON)
        } else {
            // Map<*, *> support for malformed request payloads
            val jsonPayload = if (payload is Map<*, *> || payload::class.annotations.any { it is JsonDeserialize }) {
                defaultObjMapper.writeValueAsString(payload)
            } else {
                payload.toString()
            }

            requestBuilder
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(Charsets.UTF_8)
                .content(jsonPayload)
        }
    }

    protected fun MockHttpServletRequestBuilder.send(): ResultActions = mockMvc.perform(this)
    // endregion

    fun ResultActions.expect4xx(httpStatus: HttpStatus? = HttpStatus.BAD_REQUEST): ErrorResponseEnvelope.Body {
        val result = if (httpStatus == null) {
            this.andExpect(MockMvcResultMatchers.status().is4xxClientError).andReturn()
        } else {
            if (httpStatus.value() < 400 || httpStatus.value() > 499) {
                throw IllegalArgumentException("$httpStatus is not in range of Http 400 to 499")
            }

            this.andExpect(MockMvcResultMatchers.status().`is`(httpStatus.value())).andReturn()
        }

        return parse4xxResponse(result.response.contentAsString)
    }
}
