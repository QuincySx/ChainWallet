package com.smallraw.chain.lib.crypto

import com.smallraw.chain.lib.execptions.JNICallException
import com.smallraw.chain.lib.jni.Secp256k1JNI
import com.smallraw.chain.lib.random.RandomGenerator
import com.smallraw.chain.lib.util.timeDiff


object Secp256K1 {

    private val random by lazy {
        RandomGenerator.getRandom()
    }

    fun createPrivateKey(): ByteArray {
        val bytes = ByteArray(32)
        random.nextBytes(bytes)
        return bytes
    }

    fun createPublicKey(privateKey: ByteArray, compressed: Boolean = true): ByteArray =
        Secp256k1JNI.createPublicKey(privateKey, compressed) ?: throw JNICallException()

    fun sign(privateKey: ByteArray, message: ByteArray): ByteArray =
        Secp256k1JNI.sign(privateKey, message) ?: throw JNICallException()

    fun verify(publicKey: ByteArray, signature: ByteArray, message: ByteArray): Boolean =
        Secp256k1JNI.verify(publicKey, signature, message)
}