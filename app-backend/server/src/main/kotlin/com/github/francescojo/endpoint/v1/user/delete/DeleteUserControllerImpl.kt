/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.endpoint.v1.user.delete

import com.github.francescojo.core.domain.user.UserId
import com.github.francescojo.core.domain.user.usecase.DeleteUserUseCase
import com.github.francescojo.lib.webApi.response.SimpleResponse
import com.github.francescojo.endpoint.v1.user.DeleteUserController
import io.hypersistence.tsid.TSID
import org.springframework.web.bind.annotation.RestController

/**
 * @since 2021-08-10
 */
@RestController
internal class DeleteUserControllerImpl(
    private val useCase: DeleteUserUseCase
) : DeleteUserController {
    override fun delete(id: TSID): SimpleResponse<Boolean> {
        useCase.deleteUserById(UserId(id))

        return SimpleResponse(true)
    }
}
