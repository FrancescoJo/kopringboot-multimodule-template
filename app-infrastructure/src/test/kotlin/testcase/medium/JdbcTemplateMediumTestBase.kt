/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package testcase.medium

import com.github.francescojo.core.CoreKopringApplication
import com.github.francescojo.core.CoreKopringApplicationImpl
import com.github.francescojo.core.appconfig.LoggerConfig
import com.github.francescojo.lib.annotation.MediumTest
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

/**
 * @since 2021-08-10
 */
@JdbcTest
// Overrides configuration declared in yml
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(
    classes = [
        CoreKopringApplicationImpl::class,
        LoggerConfig::class
    ],
)
@ComponentScan(
    basePackages = [
        CoreKopringApplication.PACKAGE_NAME
    ]
)
@ActiveProfiles("mediumTest")
@MediumTest
class JdbcTemplateMediumTestBase

