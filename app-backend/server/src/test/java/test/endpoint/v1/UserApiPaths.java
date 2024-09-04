/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package test.endpoint.v1;

import com.github.francescojo.endpoint.v1.ApiPathsV1;
import com.github.francescojo.endpoint.v1.ApiVariableV1;
import io.hypersistence.tsid.TSID;

import static com.github.francescojo.lib.codec.TsidCodecUtils.tsidToString;

/**
 * @since 2024-09-04
 */
public class UserApiPaths {
    public static String usersId(final TSID userId) {
        return ApiPathsV1.USERS_ID.replace(ApiVariableV1.PATH_ID, tsidToString(userId));
    }
}
