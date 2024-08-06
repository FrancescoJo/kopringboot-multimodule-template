/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package testcase.medium

import com.github.francescojo.core.domain.user.usecase.CreateUserUseCase
import com.github.francescojo.core.domain.user.usecase.DeleteUserUseCase
import com.github.francescojo.core.domain.user.usecase.EditUserUseCase
import com.github.francescojo.core.domain.user.usecase.FindUserUseCase
import com.github.francescojo.endpoint.health.HealthControllerImpl
import com.github.francescojo.endpoint.v1.user.create.CreateUserControllerImpl
import com.github.francescojo.endpoint.v1.user.delete.DeleteUserControllerImpl
import com.github.francescojo.endpoint.v1.user.edit.EditUserControllerImpl
import com.github.francescojo.endpoint.v1.user.get.GetUserControllerImpl
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.bind.annotation.RestController

/**
 * This class is for sharing application contexts across the medium test suites, to improve test execution time.
 *
 * One tedious thing is, we have to write down all [RestController] implementations here in [ContextConfiguration]
 * section to take the effect.
 *
 * @since 2021-08-10
 */
@ContextConfiguration(
    classes = [
        HealthControllerImpl::class,
        CreateUserControllerImpl::class,
        GetUserControllerImpl::class,
        EditUserControllerImpl::class,
        DeleteUserControllerImpl::class,
    ],
)
class ControllerMediumTestBase : MockMvcMediumTestBase() {
    @MockBean
    protected lateinit var createUserUseCase: CreateUserUseCase

    @MockBean
    protected lateinit var findUserUseCase: FindUserUseCase

    @MockBean
    protected lateinit var editUserUseCase: EditUserUseCase

    @MockBean
    protected lateinit var deleteUserUseCase: DeleteUserUseCase
}
