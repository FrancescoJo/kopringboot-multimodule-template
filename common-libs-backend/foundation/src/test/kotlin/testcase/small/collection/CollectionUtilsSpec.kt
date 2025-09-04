/*
 * kopringboot-multimodule-template
 * Distributed under MIT licence
 */
package testcase.small.collection

import com.github.francescojo.lib.annotation.SmallTest
import com.github.francescojo.lib.collection.randomEnum
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

/**
 * @since 2024-08-20
 */
@SmallTest
internal class CollectionUtilsSpec {
    enum class SampleEnum {
        ENTRY_1,
        ENTRY_2,
        NEVER;
    }

    @DisplayName("randomEnum should return a random enum value from the given enum class")
    @Test
    fun randomEnumSpec() {
        assertAll(
            { randomEnum<SampleEnum>()::class shouldBe SampleEnum::class },
            { randomEnum<SampleEnum> { it != SampleEnum.NEVER } shouldNotBe SampleEnum.NEVER }
        )
    }
}
