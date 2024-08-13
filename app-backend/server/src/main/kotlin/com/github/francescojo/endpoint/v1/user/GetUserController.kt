/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.endpoint.v1.user

import com.github.francescojo.endpoint.v1.ApiPathsV1
import com.github.francescojo.endpoint.v1.ApiVariableV1
import com.github.francescojo.endpoint.v1.user.common.UserResponse
import io.hypersistence.tsid.TSID
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

/**
 * ```
 * GET /v1/users/{id}
 *
 * Content-Type: application/json
 * ```
 *
 * @since 2021-08-10
 */
@RequestMapping(
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
interface GetUserController {
    @RequestMapping(
        path = [ApiPathsV1.USERS_ID],
        method = [RequestMethod.GET]
    )
    fun get(@PathVariable(ApiVariableV1.ID) id: TSID): UserResponse
}
