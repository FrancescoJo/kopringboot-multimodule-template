/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package test.endpoint.v1.user

import com.github.francescojo.core.domain.user.UserId
import com.github.francescojo.endpoint.v1.ApiPathsV1
import com.github.francescojo.endpoint.v1.user.common.UserResponse
import com.github.francescojo.endpoint.v1.user.create.CreateUserRequest
import com.github.francescojo.endpoint.v1.user.edit.EditUserRequest
import io.restassured.response.Response
import org.springframework.restdocs.payload.FieldDescriptor
import test.endpoint.v1.ApiPathsV1TestSupport.usersId
import test.endpoint.v1.user.UserApiDtoTestSupport.random
import testcase.large.endpoint.EndpointLargeTestBase
import java.net.URI

/**
 * @since 2022-09-10
 */
object UserApiTestSupport {
    fun EndpointLargeTestBase.createUserApi(
        requestPayload: CreateUserRequest,
        requestFields: List<FieldDescriptor>? = null,
        responseFields: List<FieldDescriptor>? = null,
    ): Response {
        return request()
            .apply {
                if (requestFields != null && responseFields != null) {
                    this.withDocumentation(URI.create(ApiPathsV1.USERS).toRelativePath(), requestFields, responseFields)
                }
            }
            .body(requestPayload)
            .post(ApiPathsV1.USERS)
    }

    fun EndpointLargeTestBase.getUserApi(
        userId: UserId,
        responseFields: List<FieldDescriptor>? = null,
    ): Response {
        return request()
            .apply {
                if (responseFields != null) {
                    this.withDocumentation(URI.create(ApiPathsV1.USERS_ID).toRelativePath(), null, responseFields)
                }
            }
            .get(ApiPathsV1.usersId(userId))
    }

    fun EndpointLargeTestBase.editUserApi(
        userId: UserId,
        requestPayload: EditUserRequest,
        requestFields: List<FieldDescriptor>? = null,
        responseFields: List<FieldDescriptor>? = null,
    ): Response {
        return request()
            .apply {
                if (responseFields != null) {
                    this.withDocumentation(URI.create(ApiPathsV1.USERS_ID).toRelativePath(), requestFields, responseFields)
                }
            }
            .body(requestPayload)
            .patch(ApiPathsV1.usersId(userId))
    }

    fun EndpointLargeTestBase.deleteUserApi(
        userId: UserId,
        responseFields: List<FieldDescriptor>? = null,
    ): Response {
        return request()
            .apply {
                if (responseFields != null) {
                    this.withDocumentation(URI.create(ApiPathsV1.USERS_ID).toRelativePath(), null, responseFields)
                }
            }
            .delete(ApiPathsV1.usersId(userId))
    }

    fun EndpointLargeTestBase.createRandomUser(
        request: CreateUserRequest = CreateUserRequest.random()
    ): UserResponse {
        return createUserApi(request).expect2xx(UserResponse::class)
    }
}
