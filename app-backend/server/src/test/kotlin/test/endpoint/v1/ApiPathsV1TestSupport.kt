/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package test.endpoint.v1

import com.github.francescojo.endpoint.v1.ApiPathsV1
import com.github.francescojo.endpoint.v1.ApiVariableV1
import java.util.*

fun ApiPathsV1.usersId(id: UUID): String =
    USERS_ID.replace(ApiVariableV1.PATH_ID, id.toString())
