/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.webApi.response.base

/**
 * @since 2021-08-10
 */
data class OkResponseEnvelope<T>(override val body: T?) : ResponseEnvelope<T>(Type.OK)
