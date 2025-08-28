/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.endpoint.v1.user

import com.github.francescojo.endpoint.v1.ApiPathsV1
import com.github.francescojo.endpoint.v1.ApiVariableV1
import com.github.francescojo.endpoint.v1.user.common.UserResponse
import com.github.francescojo.endpoint.v1.user.edit.EditUserRequest
import io.hypersistence.tsid.TSID
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

/**
 * ```
 * PATCH /v1/users/{id}
 *
 * Content-Type: application/json
 * ```
 *
 * @since 2021-08-10
 */
@RequestMapping(
    produces = [MediaType.APPLICATION_JSON_VALUE],
    consumes = [MediaType.APPLICATION_JSON_VALUE]
)
@Tag(
    name = "Edit User",
    description = "Modify user information"
)
interface EditUserController {
    @Operation(
        summary = "Modify user information",
        description = "Modify a user with given information.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "User information modified successfully"
            ),
            ApiResponse(
                responseCode = "409",
                description = "User with same nickname exists. Error context: `USER_BY_NICKNAME_DUPLICATED`",
            ),
            ApiResponse(
                responseCode = "409",
                description = "User with same email exists. Error context: `USER_BY_EMAIL_DUPLICATED`",
            )
        ]
    )
    @RequestMapping(
        path = [ApiPathsV1.USERS_ID],
        method = [RequestMethod.PATCH]
    )
    fun edit(
        @PathVariable(ApiVariableV1.ID) id: TSID,
        @Valid @RequestBody req: EditUserRequest
    ): UserResponse
}
