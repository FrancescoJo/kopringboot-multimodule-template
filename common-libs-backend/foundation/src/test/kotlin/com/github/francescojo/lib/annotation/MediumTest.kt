/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.annotation

import org.junit.jupiter.api.Tag

/**
 * Annotates that marked test suite is classified as medium-sized JUnit tests.
 *
 * The Medium test covers the same range as [SmallTest]s; however, we can construct test logic directly access to
 * controllable external environments like network, database, filesystem, etc.
 *
 * @since 2020-02-14
 */
@Tag("mediumTest")
annotation class MediumTest
