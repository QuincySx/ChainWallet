package com.smallraw.chain.lib.crypto

import com.smallraw.chain.lib.execptions.JNICallException
import com.smallraw.chain.lib.jni.Ed25519JNI
import com.smallraw.chain.lib.random.RandomGenerator

object Ed25519 {

    private val random by lazy {
        RandomGenerator.getRandom()
    }

    fun createPrivateKey(): ByteArray {
        val bytes = ByteArray(32)
        random.nextBytes(bytes)
        return bytes
    }

    fun createPublicKey(privateKey: ByteArray): ByteArray =
        Ed25519JNI.createPublicKey(privateKey)?:throw JNICallException()

    fun sign(privateKey: ByteArray, message: ByteArray): ByteArray =
        Ed25519JNI.sign(privateKey, message) ?: throw JNICallException()

    fun verify(publicKey: ByteArray, signature: ByteArray, message: ByteArray): Boolean =
        Ed25519JNI.verify(publicKey, signature, message)
}