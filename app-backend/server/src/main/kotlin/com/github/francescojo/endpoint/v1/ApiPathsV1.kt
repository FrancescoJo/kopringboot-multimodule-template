/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.endpoint.v1

/**
 * @since 2021-08-10
 */
object ApiPathsV1 {
    const val V1 = "/v1"

    const val USERS = "$V1/users"
    const val USERS_ID = "$USERS/${ApiVariableV1.PATH_ID}"
}
