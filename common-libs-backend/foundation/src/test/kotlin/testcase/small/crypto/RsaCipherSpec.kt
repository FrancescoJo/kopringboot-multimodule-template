/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package testcase.small.crypto

import com.github.francescojo.lib.annotation.SmallTest
import com.github.francescojo.lib.crypto.RsaCipher
import io.kotest.matchers.shouldBe
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.security.KeyPairGenerator
import java.security.SecureRandom

/**
 * @since 2022-04-13
 */
@SmallTest
internal class RsaCipherSpec {
    @ParameterizedTest(name = "It should decrypt encrypted message by RSA{0} algorithm with arbitrary key pairs")
    @ValueSource(ints = [1024, 2048])
    fun `It should decrypt encrypted message by RSA algorithm with arbitrary key pairs`(modulusSize: Int) {
        // given:
        val plainText = "THIS IS A PLAIN TEXT"
        val rawKeyPair = testKeyPairGenerator().run {
            initialize(modulusSize, SecureRandom())
            return@run generateKeyPair()
        }

        // when:
        val encrypted = RsaCipher.encrypt(rawKeyPair.public, plainText.toByteArray())

        // and:
        val decrypted = RsaCipher.decrypt(rawKeyPair.private, encrypted)

        // then:
        val decryptedText = String(decrypted)

        // expect:
        decryptedText shouldBe plainText
    }

    private fun testKeyPairGenerator() = KeyPairGenerator.getInstance(RsaCipher.ALGORITHM)
}
