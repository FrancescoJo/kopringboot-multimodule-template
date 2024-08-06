/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.endpoint.v1.user.delete

import com.github.francescojo.core.domain.user.usecase.DeleteUserUseCase
import com.github.francescojo.endpoint.common.response.SimpleResponse
import com.github.francescojo.endpoint.v1.user.DeleteUserController
import org.springframework.web.bind.annotation.RestController
import java.util.*

/**
 * @since 2021-08-10
 */
@RestController
internal class DeleteUserControllerImpl(
    private val useCase: DeleteUserUseCase
) : DeleteUserController {
    override fun delete(id: UUID): SimpleResponse<Boolean> {
        useCase.deleteUserById(id)

        return SimpleResponse(true)
    }
}
