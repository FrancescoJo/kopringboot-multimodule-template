/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.endpoint.v1.user

import com.github.francescojo.endpoint.v1.ApiPathsV1
import com.github.francescojo.endpoint.v1.ApiVariableV1
import com.github.francescojo.endpoint.v1.user.common.UserResponse
import com.github.francescojo.endpoint.v1.user.edit.EditUserRequest
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import java.util.*
import javax.validation.Valid

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
interface EditUserController {
    @RequestMapping(
        path = [ApiPathsV1.USERS_ID],
        method = [RequestMethod.PATCH]
    )
    fun edit(
        @PathVariable(ApiVariableV1.ID) id: UUID,
        @Valid @RequestBody req: EditUserRequest
    ): UserResponse
}
