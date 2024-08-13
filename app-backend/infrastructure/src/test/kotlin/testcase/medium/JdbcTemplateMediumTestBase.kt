/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package testcase.medium

import com.github.francescojo.infra.appconfig.LoggerConfig
import com.github.francescojo.infra.jdbc.JpaConfig
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import test.annotation.JdbcMediumTest

/**
 * @since 2021-08-10
 */
@ContextConfiguration(
    classes = [
        LoggerConfig::class,
        JpaConfig::class
    ],
)
@ComponentScan(
    basePackages = [
        "com.github.francescojo.infra.jdbc"
    ]
)
@ActiveProfiles("mediumTest")
@JdbcMediumTest
class JdbcTemplateMediumTestBase
