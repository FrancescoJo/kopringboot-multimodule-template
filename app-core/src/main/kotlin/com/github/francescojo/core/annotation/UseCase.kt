/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.core.annotation

/**
 * Annotates a programme element which is handling a vital user scenario.
 *
 * @since 2020-02-14
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class UseCase
