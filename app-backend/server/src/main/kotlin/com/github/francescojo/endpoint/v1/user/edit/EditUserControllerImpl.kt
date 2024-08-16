/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.endpoint.v1.user.edit

import com.github.francescojo.core.domain.user.UserId
import com.github.francescojo.core.domain.user.usecase.EditUserUseCase
import com.github.francescojo.endpoint.v1.user.EditUserController
import com.github.francescojo.endpoint.v1.user.common.UserResponse
import io.hypersistence.tsid.TSID
import jakarta.validation.Validator
import org.springframework.web.bind.annotation.RestController

/**
 * @since 2021-08-10
 */
@RestController
internal class EditUserControllerImpl(
    private val useCase: EditUserUseCase,
    private val validator: Validator
) : EditUserController {
    override fun edit(id: TSID, req: EditUserRequest): UserResponse {
        val editedUser = useCase.editUser(UserId(id), req.toMessage(validator))

        return UserResponse.from(editedUser)
    }
}
