/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.ComponentScan

fun main(args: Array<String>) {
    KopringApplicationBootstrap().start(args)
}

/**
 * @since 2021-08-10
 */
@SpringBootApplication
@ComponentScan(
    basePackages = [
        KopringApplicationBootstrap.PACKAGE_NAME
    ]
)
class KopringApplicationBootstrap {
    fun start(args: Array<String>) {
        // This logic is called only once - therefore, we ignore this warning.
        @Suppress("SpreadOperator")
        SpringApplicationBuilder(KopringApplicationBootstrap::class.java)
            .build()
            .run(*args)
    }

    companion object {
        const val PACKAGE_NAME = "com.github.francescojo"
    }
}
