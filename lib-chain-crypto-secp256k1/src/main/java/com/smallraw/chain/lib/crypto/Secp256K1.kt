package com.smallraw.chain.lib.crypto

import com.smallraw.chain.lib.jni.Secp256k1JNI
import com.smallraw.chain.lib.random.RandomGenerator
import com.smallraw.chain.lib.util.timeDiff


object Secp256K1 {
    private val secp256k1Wrapper: Secp256k1JNI by lazy {
        Secp256k1JNI()
    }

    private val random by lazy {
        RandomGenerator.getRandom()
    }

    fun createPrivateKey(): ByteArray {
        val bytes = ByteArray(32)
        random.nextBytes(bytes)
        return bytes
    }

    fun createPublicKey(privateKey: ByteArray): ByteArray? =
        secp256k1Wrapper.createPublicKey(privateKey, false)

    fun sign(privateKey: ByteArray, message: ByteArray): ByteArray? =
        secp256k1Wrapper.sign(privateKey, message)

    fun verify(publicKey: ByteArray, signature: ByteArray, message: ByteArray): Boolean =
        secp256k1Wrapper.verify(publicKey, signature, message)
}