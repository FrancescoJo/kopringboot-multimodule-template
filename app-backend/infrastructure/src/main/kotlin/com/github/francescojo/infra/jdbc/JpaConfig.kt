/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.infra.jdbc

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

/**
 * @since 2024-08-13
 */
@Configuration
@EntityScan(JpaConfig.PACKAGE_NAME_JPA_ENTITY)
@EnableJpaRepositories(JpaConfig.PACKAGE_NAME_JPA_REPOSITORY)
class JpaConfig {
    companion object {
        const val PACKAGE_NAME_JPA_ENTITY = "com.github.francescojo.infra.jdbc"
        const val PACKAGE_NAME_JPA_REPOSITORY = "com.github.francescojo.infra.jdbc"
    }
}
