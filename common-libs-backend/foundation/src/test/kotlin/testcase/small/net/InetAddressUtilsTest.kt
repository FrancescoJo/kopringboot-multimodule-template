/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package testcase.small.net

import com.github.francescojo.lib.annotation.SmallTest
import com.github.francescojo.lib.net.InetAddresses
import com.github.francescojo.lib.net.isNullOrEmpty
import com.github.francescojo.lib.net.toInetAddress
import com.github.francescojo.lib.net.toIpV6AddressBytes
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.net.InetAddress

/**
 * @since 2022-02-08
 */
@SmallTest
internal class InetAddressUtilsTest {
    @ParameterizedTest(name = "InetAddress conversion for ''{0}''")
    @ValueSource(strings = ["localhost", "8.8.8.8"])
    fun `InetAddress to bytes conversion and vice versa should be exactly same`(hostname: String) {
        // when:
        val sourceAddress = InetAddress.getByName(hostname)

        // then:
        val bytes = sourceAddress.toIpV6AddressBytes()

        // expect:
        sourceAddress shouldBe bytes.toInetAddress()
    }

    @Test
    fun `InetAddress#isEmpty works properly on InetAddress#empty result`() {
        // given:
        val emptyInetAddress = InetAddresses.EMPTY

        // expect:
        emptyInetAddress.isNullOrEmpty() shouldBe true
    }
}
