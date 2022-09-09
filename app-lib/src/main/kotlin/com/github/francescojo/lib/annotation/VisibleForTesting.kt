/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.annotation

/**
 * Annotates a programme element which widened its visibility other than necessary,
 * is only for use in test codes.
 *
 * @since 2020-02-14
 */
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FIELD,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.TYPE,
)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class VisibleForTesting
