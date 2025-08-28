/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.webApi.response.base

import io.swagger.v3.oas.annotations.media.Schema

/**
 * @since 2021-08-10
 */
@Schema(name = "OkResponseEnvelope")
data class OkResponseEnvelope<T>(override val body: T?) : ResponseEnvelope<T>(Type.OK)
