/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package test.endpoint.v1.user

import com.github.francescojo.endpoint.v1.ApiPathsV1
import com.github.francescojo.endpoint.v1.user.common.UserResponse
import com.github.francescojo.endpoint.v1.user.create.CreateUserRequest
import com.github.francescojo.endpoint.v1.user.edit.EditUserRequest
import io.restassured.response.Response
import org.springframework.restdocs.payload.FieldDescriptor
import test.endpoint.v1.usersId
import testcase.large.endpoint.EndpointLargeTestBase
import java.net.URI
import java.util.*

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
    userId: UUID,
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
    userId: UUID,
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
    userId: UUID,
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
