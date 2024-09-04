/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package test.endpoint.v1.user

import com.github.francescojo.endpoint.v1.user.create.CreateUserRequest
import com.github.francescojo.endpoint.v1.user.edit.EditUserRequest

import static test.com.github.francescojo.lib.SharedTestObjects.faker

/**
 * @since 2024-09-04
 */
class UserApiDtoGroovyTestSupport {
    static CreateUserRequest randomCreateUserRequest(final Map params = null) {
        return new CreateUserRequest(
                (params?.nickname ?: faker.name().fullName()) as String,
                (params?.email ?: faker.internet().emailAddress()) as String
        )
    }

    static EditUserRequest randomEditUserRequest(final Map params = null) {
        return new EditUserRequest(
                defaultIfUndefined(params, "nickname", faker.name().fullName()),
                defaultIfUndefined(params, "email", faker.internet().emailAddress())
        )
    }

    // Maybe this method could be extracted as utility
    private static <T> Optional<T> defaultIfUndefined(
            final Map params,
            final String key,
            final T defaultValue
    ) {
        // Ignore this dead assign for more readability
        Optional<T> value = Optional.of(defaultValue)
        if (params != null && params.containsKey(key)) {
            value = Optional.ofNullable(params.get(key) as T)
        }

        return value
    }
}
