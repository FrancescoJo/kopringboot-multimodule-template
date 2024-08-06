/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.infra.projection

import com.github.francescojo.core.domain.IdentifiableObject
import com.github.francescojo.core.domain.ValueHolder
import com.github.francescojo.core.projection.ProjectionFinder

/**
 * @since 2024-08-06
 */
abstract class AbstractProjectionFinderTemplate<T : IdentifiableObject<ID>, ID : ValueHolder<*>> :
    ProjectionFinder<T, ID>
