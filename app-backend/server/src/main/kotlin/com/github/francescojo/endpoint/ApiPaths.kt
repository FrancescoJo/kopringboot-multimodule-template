/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.endpoint

import com.github.francescojo.endpoint.v1.ApiPathsV1

/**
 * @since 2021-08-10
 */
object ApiPaths {
    /** Used by Spring default */
    const val ERROR = "/error"

    /** Replace this to spring-actuator if needed */
    const val HEALTH = "/health"

    const val LATEST_VERSION = ApiPathsV1.V1
}
