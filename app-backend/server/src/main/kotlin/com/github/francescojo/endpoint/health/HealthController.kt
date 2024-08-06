/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.endpoint.health

import com.github.francescojo.endpoint.ApiPaths
import com.github.francescojo.endpoint.common.response.SimpleResponse
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * ```
 * GET /health
 *
 * Content-Type: application/json
 * ```
 *
 * @since 2021-08-10
 */
@RequestMapping(
    produces = [MediaType.APPLICATION_JSON_VALUE],
)
interface HealthController {
    @RequestMapping(
        path = [ApiPaths.HEALTH],
        method = [RequestMethod.GET]
    )
    fun health(): SimpleResponse<String>
}

@RestController
internal class HealthControllerImpl : HealthController {
    override fun health() = SimpleResponse("OK")
}
