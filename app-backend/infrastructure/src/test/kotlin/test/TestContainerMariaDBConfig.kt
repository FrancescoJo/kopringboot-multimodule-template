/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package test

import com.github.dockerjava.api.model.ExposedPort
import com.github.dockerjava.api.model.HostConfig
import com.github.dockerjava.api.model.PortBinding
import com.github.dockerjava.api.model.Ports
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.testcontainers.containers.MariaDBContainer

/**
 * It is very convenient that using automatic lifecycle management of TestContainers on JUnit Jupiter platform;
 * however it has a huge drawback. It could take a long time to run all tests because the automatic lifecycle
 * management starts and stops test containers on each test suites. In this case, tests on some logic depends on
 * infrastructures like MySQL or MariaDB which requires quite long time to start up, will consume a lot of time.
 *
 * That would be a correct way on testing perspective, but impractical on actual business development.
 * So we are managing TestContainer lifecycle by manual, using this class.
 *
 * Reference: [Manual container lifecycle control](https://www.testcontainers.org/test_framework_integration/manual_lifecycle_control/#singleton-containers)
 *
 * @since 2022-06-20
 */
@TestConfiguration
@Order(value = Ordered.HIGHEST_PRECEDENCE)
class TestContainerMariaDBConfig {
    @PostConstruct
    fun onApplicationStart() {
        if (!MYSQL_CONTAINER_API.isRunning) {
            MYSQL_CONTAINER_API.start()
        }
    }

    @PreDestroy
    fun onApplicationDestroy() {
        MYSQL_CONTAINER_API.stop()
    }

    companion object {
        private const val MYSQL_IMAGE_NAME = "mariadb:10.10.2"
        private const val DEFAULT_MYSQL_PORT = 3306
        // Must be match to spring.datasource.url in application.yml, we can automise this later on by parsing it
        private const val API_MYSQL_PORT = 53306
        private const val API_MYSQL_DATABASE = "kopringboot-app"
        private const val API_MYSQL_USERNAME = "test"
        private const val API_MYSQL_PASSWORD = "test"

        private val MYSQL_CONTAINER_API: MariaDBContainer<*> by lazy {
            newMariaDbContainer(
                hostPort = API_MYSQL_PORT,
                databaseName = API_MYSQL_DATABASE,
                username = API_MYSQL_USERNAME,
                password = API_MYSQL_PASSWORD
            )
        }

        @Suppress("SameParameterValue")     // We can utilise this method later on to general purposes
        private fun newMariaDbContainer(
            hostPort: Int,
            databaseName: String,
            username: String,
            password: String
        ): MariaDBContainer<*> = MariaDBContainer(MYSQL_IMAGE_NAME)
            .withCreateContainerCmdModifier {
                it.withHostConfig(
                    HostConfig().withPortBindings(
                        PortBinding(Ports.Binding.bindPort(hostPort), ExposedPort(DEFAULT_MYSQL_PORT))
                    )
                )
            }
            .withReuse(true)
            .withDatabaseName(databaseName)
            .withUsername(username)
            .withPassword(password)

        fun forceStartContainer() {
            if (!MYSQL_CONTAINER_API.isRunning) {
                MYSQL_CONTAINER_API.start()
            }
        }
    }
}
