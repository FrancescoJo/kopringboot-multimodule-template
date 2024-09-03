/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package testcase.small.crypto

import com.github.francescojo.lib.annotation.SmallTest
import com.github.francescojo.lib.crypto.AesCipher
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.random.Random

/**
 * @since 2022-04-13
 */
@SmallTest
internal class AesCipherSpec {
    @ParameterizedTest(name = "It should decrypt encrypted message by {0} algorithm with arbitrary key and iv")
    @MethodSource("aesKeyAndIv")
    fun `It should decrypt encrypted message by AES algorithm with arbitrary key and iv`(
        @Suppress("UNUSED_PARAMETER") testName: String,
        key: ByteArray,
        iv: ByteArray
    ) {
        // given:
        val plainText = "THIS IS A PLAIN TEXT"

        // when:
        val encrypted = AesCipher.encrypt(key, iv, plainText.toByteArray())

        // and:
        val decrypted = AesCipher.decrypt(key, iv, encrypted)

        // then:
        val decryptedText = String(decrypted)

        // expect:
        assertAll(
            /*
             * For AES128 the block cipher size is 16 bytes(128 bits),
             * and for AES256 the block cipher size is 32 bytes(256 bits).
             * Also, the plaintext is 20 bytes long - therefore, the result is 32 bytes(256 bits) in both cases.
             */
            { encrypted.size shouldBe 32 },
            { decryptedText shouldBe plainText }
        )
    }

    companion object {
        @JvmStatic
        fun aesKeyAndIv(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "AES128",
                Random.Default.nextBytes(16),   // 16 x 8 = 128 Bit
                Random.Default.nextBytes(16)
            ),
            Arguments.of(
                "AES256",
                Random.Default.nextBytes(32),   // 32 x 8 = 256 Bit
                Random.Default.nextBytes(16)
            )
        )
    }
}
