/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.annotation

/**
 * Instructs the Kotlin compiler that this class must be open before compilation, although the default
 * inheritance policy is declared as `final`.
 *
 * This annotation is useful for test scenarios that are requiring test double(s) which is/are declared as
 * final class in design.
 *
 * Read [kotlin-allopen](https://kotlinlang.org/docs/all-open-plugin.html) compiler plugin documentation for
 * more information.
 *
 * @since 2022-02-14
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class OpenForTesting
