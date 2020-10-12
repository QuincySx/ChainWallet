package com.smallraw.lib.crypto

import com.smallraw.chain.lib.core.execptions.JNICallException
import com.smallraw.chain.lib.core.random.RandomGenerator
import com.smallraw.crypto.jni.Secp256k1JNI


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
        Secp256k1JNI().createPublicKey(privateKey, compressed) ?: throw JNICallException()

    fun sign(privateKey: ByteArray, message: ByteArray): ByteArray =
        Secp256k1JNI().sign(privateKey, message) ?: throw JNICallException()

    fun verify(publicKey: ByteArray, signature: ByteArray, message: ByteArray): Boolean =
        Secp256k1JNI().verify(publicKey, signature, message)
}