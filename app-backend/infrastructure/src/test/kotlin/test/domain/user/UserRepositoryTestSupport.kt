/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package test.domain.user

import com.github.francescojo.core.domain.user.UserId
import com.github.francescojo.core.domain.user.repository.UserRepository
import io.hypersistence.tsid.TSID
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * This object is designed for Java/Groovy interop purposes.
 *
 * @since 2024-09-04
 */
object UserRepositoryTestSupport {
    private val log: Logger by lazy { LoggerFactory.getLogger(UserRepositoryTestSupport::class.simpleName) }

    @JvmStatic
    fun deleteUserById(userRepository: UserRepository, id: TSID?) {
        if (id == null) {
            return
        }

        try {
            userRepository.deleteById(UserId(id))
        } catch (e: Throwable) {
            log.warn("User deletion by '$id' has been failed.", e)
        }
    }
}
