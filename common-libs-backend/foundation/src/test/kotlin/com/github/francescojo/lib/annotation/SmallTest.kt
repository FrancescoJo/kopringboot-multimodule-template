/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.annotation

import org.junit.jupiter.api.Tag

/**
 * Annotates that marked test suite is classified as small-sized JUnit tests.
 *
 * We perform small/unit tests to confirm a single programme component is fully functional.
 * We instrument this kind of tests very frequently during the development stage -
 * therefore, it should be small, fast and concise.
 *
 * Introducing test doubles, such as mocks, spies, etc. for speed and stability is encouraged on this stage.
 *
 * @since 2020-02-14
 */
@Tag("smallTest")
annotation class SmallTest
