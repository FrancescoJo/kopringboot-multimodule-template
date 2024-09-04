/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package test.com.github.francescojo.lib

import com.github.javafaker.Faker

/**
 * @since 2021-08-10
 */
object SharedTestObjects {
    /**
     * Since the Faker object reads the internal yml every time when it is created,
     * and it causes a performance degradation during tests.
     *
     * This approach is for test speedup.
     *
     * However, we could not discover whether sharing Faker instance is thread-safe or not.
     */
    @JvmStatic
    val faker: Faker by lazy { Faker() }
}
