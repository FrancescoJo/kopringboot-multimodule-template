/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.net

import java.net.URI

/**
 * Splits path segments of given URI into a list of string.
 *
 * @since 2022-02-14
 */
@SuppressWarnings("ReturnCount")
fun URI.pathSegments(): List<String> {
    var path = this.path
    when {
        path.isNullOrEmpty() -> return emptyList()
        path == "/"          -> return listOf("/")
    }

    if (path.startsWith("/")) {
        path = path.substring(1)
    }

    return path.split("/").filter { it.isNotEmpty() }
}
