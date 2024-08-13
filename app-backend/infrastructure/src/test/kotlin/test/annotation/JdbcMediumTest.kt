/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package test.annotation

import com.github.francescojo.lib.annotation.MediumTest
import jakarta.transaction.Transactional
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTypeExcludeFilter
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTypeExcludeFilter
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTypeExcludeFilter
import org.springframework.test.context.BootstrapWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.lang.annotation.Inherited

/**
 * Custom annotation to enable both Jdbc and JPA repositories for testing.
 *
 * @since 2024-08-13
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Inherited
@EnableAutoConfiguration
@BootstrapWith(JdbcMediumTestContextBootstrapper::class)
@ExtendWith(SpringExtension::class)
@OverrideAutoConfiguration(enabled = false)
@TypeExcludeFilters(
    value = [
        DataJpaTypeExcludeFilter::class,
        JdbcTypeExcludeFilter::class,
//        DataJdbcTypeExcludeFilter::class
    ]
)
@Transactional
@AutoConfigureCache
@AutoConfigureDataJpa
@AutoConfigureJdbc
@AutoConfigureDataJdbc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)    // Overrides configuration declared in yml
@AutoConfigureTestEntityManager
@ImportAutoConfiguration
@MediumTest
annotation class JdbcMediumTest
