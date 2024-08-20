/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.crypto

import java.security.PrivateKey
import java.security.PublicKey
import javax.crypto.Cipher

/**
 * @since 2022-04-13
 */
object RsaCipher {
    const val ALGORITHM = "RSA"

    private val RSA_CIPHER: Cipher
        get() = Cipher.getInstance(ALGORITHM)

    fun encrypt(publicKey: PublicKey, plainText: ByteArray): ByteArray =
        RSA_CIPHER
            .apply { init(Cipher.ENCRYPT_MODE, publicKey) }
            .doFinal(plainText)

    fun decrypt(privateKey: PrivateKey, cipherText: ByteArray): ByteArray =
        RSA_CIPHER
            .apply { init(Cipher.DECRYPT_MODE, privateKey) }
            .doFinal(cipherText)
}
