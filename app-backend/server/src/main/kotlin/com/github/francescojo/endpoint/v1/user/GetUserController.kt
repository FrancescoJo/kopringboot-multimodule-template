/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.endpoint.v1.user

import com.github.francescojo.endpoint.v1.ApiPathsV1
import com.github.francescojo.endpoint.v1.ApiVariableV1
import com.github.francescojo.endpoint.v1.user.common.UserResponse
import io.hypersistence.tsid.TSID
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
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
@Tag(
    name = "Get User",
    description = "Get user information"
)
interface GetUserController {
    @Operation(
        summary = "Get user information by user ID",
        description = "Retrieves user information by given ID.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "User is found, and user information is retrieved successfully.",
            ),
            ApiResponse(
                responseCode = "404",
                description = "User with given ID does not exist. Error context: `USER_BY_ID_NOT_FOUND`",
            )
        ]
    )
    @RequestMapping(
        path = [ApiPathsV1.USERS_ID],
        method = [RequestMethod.GET]
    )
    fun get(@PathVariable(ApiVariableV1.ID) id: TSID): UserResponse
}
