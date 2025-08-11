/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.admin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

fun main(args: Array<String>) {
    AdminApplicationBootstrap().start(args)
}

/**
 * @since 2021-08-10
 */
@SpringBootApplication(
    scanBasePackages = [
        AdminApplicationBootstrap.PACKAGE_NAME
    ]
)
class AdminApplicationBootstrap {
    fun start(args: Array<String>) {
        // This logic is called only once - therefore, we ignore this warning.
        @Suppress("SpreadOperator")
        SpringApplicationBuilder(AdminApplicationBootstrap::class.java)
            .build()
            .run(*args)
    }

    companion object {
        const val PACKAGE_NAME = "com.github.francescojo.admin"
    }
}
