/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package test.endpoint.v1

import com.github.francescojo.core.domain.user.UserId
import com.github.francescojo.endpoint.v1.ApiPathsV1
import com.github.francescojo.endpoint.v1.ApiVariableV1
import com.github.francescojo.util.WebMvcBindUtils

/**
 * @since 2022-09-10
 */
object ApiPathsV1TestSupport : WebMvcBindUtils {
    fun ApiPathsV1.usersId(id: UserId): String =
        USERS_ID.replace(ApiVariableV1.PATH_ID, id.value.serialise())
}
