/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package test.endpoint.v1.user

import com.github.francescojo.endpoint.v1.ApiPathsV1
import com.github.francescojo.endpoint.v1.user.common.UserResponse
import com.github.francescojo.endpoint.v1.user.create.CreateUserRequest
import com.github.francescojo.endpoint.v1.user.edit.EditUserRequest
import io.hypersistence.tsid.TSID
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import org.springframework.restdocs.payload.FieldDescriptor
import test.endpoint.v1.UserApiPaths
import testcase.large.endpoint.EndpointLargeTestBase

import static UserApiDtoGroovyTestSupport.randomCreateUserRequest

/**
 * @since 2024-09-04
 */
class UserApiGroovyTestSupport {
    static final Response createUserApi(
            final EndpointLargeTestBase self,
            final CreateUserRequest request,
            final List<FieldDescriptor> requestFields = null,
            final List<FieldDescriptor> responseFields = null
    ) {
        return withDocumentation(self.request(), requestFields, responseFields)
                .body(request)
                .post(ApiPathsV1.USERS)
    }

    static final Response getUserApi(
            final EndpointLargeTestBase self,
            final TSID userId,
            final List<FieldDescriptor> responseFields = null
    ) {
        return withDocumentation(self.request(), null, responseFields)
                .get(UserApiPaths.usersId(userId))
    }

    static final Response editUserApi(
            final EndpointLargeTestBase self,
            final TSID userId,
            final EditUserRequest request,
            final List<FieldDescriptor> requestFields = null,
            final List<FieldDescriptor> responseFields = null
    ) {
        return withDocumentation(self.request(), requestFields, responseFields)
                .body(request)
                .patch(UserApiPaths.usersId(userId))
    }

    static final Response deleteUserApi(
            final EndpointLargeTestBase self,
            final TSID userId,
            final List<FieldDescriptor> responseFields = null
    ) {
        return withDocumentation(self.request(), null, responseFields)
                .delete(UserApiPaths.usersId(userId))
    }

    static final UserResponse createRandomUserApi(
            final EndpointLargeTestBase self,
            final CreateUserRequest request = randomCreateUserRequest()
    ) {
        return self.expect2xx(createUserApi(self, request), UserResponse.class).get()
    }

    private static RequestSpecification withDocumentation(
            final RequestSpecification rs,
            final List<FieldDescriptor> requestFields,
            final List<FieldDescriptor> responseFields
    ) {
        if (requestFields != null && responseFields != null) {
            // self.withDocumentation(rs, self.uriToRelativePath(URI.create(ApiPathsV1.USERS)), requestFields, responseFields)
        }

        return rs
    }
}
