/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.annotation

import org.junit.jupiter.api.Tag

/**
 * Annotates that marked test suite is classified as large-sized JUnit tests.
 *
 * We perform large/integration tests to confirm a combination of programme components are fully functional.
 * Test doubles such as mocks, spies, etc. should be avoided as much as possible on this test stage.
 *
 * @since 2020-02-14
 */
@Tag("largeTest")
annotation class LargeTest
