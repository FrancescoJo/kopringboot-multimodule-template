package com.github.francescojo.appconfig

import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.v3.core.util.Json
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.servers.Server
import org.slf4j.Logger
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * @since 2021-08-10
 */
@Configuration
class SpringdocOpenApiConfig(
    private val defaultObjectMapper: ObjectMapper,
    private val log: Logger
) : WebMvcConfigurer {
    private fun attachSchemas(context: Components) {
        val resolver = PathMatchingResourcePatternResolver()
        val pattern = "classpath:/${API_SCHEMA_DIR}/**/*.${SCHEMA_POSTFIX}"
        val schemaCandidates: Array<Resource> = resolver.getResources(pattern)

        for (schema in schemaCandidates) {
            val resourceUri = schema.uri.toString()
            val schemaPath = resourceUri.substringAfter(API_SCHEMA_DIR).substringBeforeLast(".${SCHEMA_POSTFIX}").let {
                if (it.startsWith("/")) {
                    it.substring(1)
                } else {
                    it
                }
            }
            val schemaName = schemaPath.replace("/", ".")

            log.trace("Found JSON schema: {} << {}", schemaName, resourceUri)

            try {
                context.addSchemas(
                    schemaName, Json.mapper().convertValue(
                        schema.inputStream.use { defaultObjectMapper.readTree(it) },
                        Schema::class.java
                    )
                )
            } catch (e: Exception) {
                log.warn("Failed to add JSON schema: {}", schemaName, e)
            }
        }
    }

    @Bean
    @Profile("local", "alpha")
    fun openAPI(
        @Value($$"${server.host}") host: String,
        @Value($$"${server.port}") port: Int,
        @Value($$"${management.info.app.name:}") appName: String,
        @Value($$"${management.info.app.version:}") version: String
    ): OpenAPI = OpenAPI()
        .servers(
            listOf(
                Server()
                    .url(
                        "http${
                            if (port == 443) {
                                "s"
                            } else {
                                ""
                            }
                        }://${host}${
                            if (port == 80) {
                                ""
                            } else {
                                ":${port}"
                            }
                        }"
                    )
                    .description("")
            )
        )
        .info(
            Info().title(appName)
                .version(version)
        )

    @Bean
    @Profile("local", "alpha")
    fun openApiCustomizer(): OpenApiCustomizer = OpenApiCustomizer { openApi ->
        // Clear all auto-generated components and replace with custom ones only
        openApi.components = Components().apply { attachSchemas(this) }
    }

    @Bean
    @Profile("local", "alpha")
    fun userApiV1(): GroupedOpenApi = GroupedOpenApi.builder()
        .group("v1-user")
        .packagesToScan(
            "com.github.francescojo.endpoint.v1.user"
        )
        // Reuse the global customizer
        .addOpenApiCustomizer(openApiCustomizer())
        .build()

    companion object {
        private const val API_SCHEMA_DIR = "docs/api-schema"
        private const val SCHEMA_POSTFIX = "schema.json"
    }
}
