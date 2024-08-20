/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.annotation

/**
 * Annotates a programme element which MAY incur a side effect,
 * by modifying the state of the given object and/or an object that includes annotated method.
 *
 * @since 2022-02-14
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class NonPureFunction
