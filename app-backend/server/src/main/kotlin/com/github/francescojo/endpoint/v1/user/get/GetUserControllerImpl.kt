/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.endpoint.v1.user.get

import com.github.francescojo.core.domain.user.UserId
import com.github.francescojo.core.domain.user.usecase.FindUserUseCase
import com.github.francescojo.endpoint.v1.user.GetUserController
import com.github.francescojo.endpoint.v1.user.common.UserResponse
import io.hypersistence.tsid.TSID
import org.springframework.web.bind.annotation.RestController

/**
 * @since 2021-08-10
 */
@RestController
internal class GetUserControllerImpl(
    private val useCase: FindUserUseCase
) : GetUserController {
    override fun get(id: TSID): UserResponse {
        val user = useCase.getUserById(UserId(id))

        return UserResponse.from(user)
    }
}
