/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package test.domain.user.projection

import com.github.francescojo.core.domain.user.UserId
import com.github.francescojo.core.domain.user.projection.UserProjection
import com.github.francescojo.core.domain.user.projection.finder.UserProjectionFinder
import test.domain.user.repository.MockUserRepository
import test.projection.AbstractMockProjectionFinder

/**
 * @since 2024-08-07
 */
class MockUserProjectionFinder(
    userRepository: MockUserRepository = MockUserRepository()
) : AbstractMockProjectionFinder<UserProjection, UserId>(), UserProjectionFinder {
    init {
        with (userRepository) {
            onSave {
                val projection = UserProjection.aggregate(it)

                save(projection)
            }
            onDelete { deleteById(it) }
            onClear { clearMocks() }
        }
    }
}
