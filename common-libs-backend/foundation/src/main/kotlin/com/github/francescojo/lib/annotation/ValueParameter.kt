/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.annotation

/**
 * Annotates a programme element that represents a simple value parameter.
 * A function that consists only of this parameter must **always** operate as a Pure Function.
 *
 * In addition, function parameters with this annotation are excluded from the Detekt's Long Parameter List check.
 *
 * @since 2023-11-02
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
annotation class ValueParameter
