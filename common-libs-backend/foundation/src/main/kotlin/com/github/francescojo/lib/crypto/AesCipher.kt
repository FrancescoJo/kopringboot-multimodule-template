/*
 * kopringboot-multimodule-monorepo-template
 * Distributed under MIT licence
 */
package com.github.francescojo.lib.crypto

import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * This object offers an AES/CBC/PKCS5 Padding AES Cipher.
 *
 * Cipher strength is determined by given key length - 16 bytes for AES128 and 32 bytes for AES256.
 *
 * The initialisation vector(iv) must be 16 bytes long.
 *
 * @since 2022-04-13
 */
object AesCipher {
    const val ALGORITHM = "AES"

    private val AES_CIPHER: Cipher
        get() = Cipher.getInstance("$ALGORITHM/CBC/PKCS5Padding")

    fun encrypt(key: ByteArray, iv: ByteArray, plainText: ByteArray): ByteArray =
        AES_CIPHER.apply {
            val keySpec = SecretKeySpec(key, ALGORITHM)
            val ivParamSpec = IvParameterSpec(iv)
            init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec)
        }.doFinal(plainText)

    fun decrypt(key: ByteArray, iv: ByteArray, cipherText: ByteArray): ByteArray =
        AES_CIPHER.apply {
            val keySpec = SecretKeySpec(key, ALGORITHM)
            val ivParamSpec = IvParameterSpec(iv)
            init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec)
        }.doFinal(cipherText)
}
