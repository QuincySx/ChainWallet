package com.smallraw.crypto

import com.smallraw.crypto.core.execptions.JNICallException
import com.smallraw.crypto.core.random.RandomGenerator
import com.smallraw.crypto.jni.Ed25519JNI

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
        Ed25519JNI().createPublicKey(privateKey)?:throw JNICallException()

    fun sign(privateKey: ByteArray, message: ByteArray): ByteArray =
        Ed25519JNI().sign(privateKey, message) ?: throw JNICallException()

    fun verify(publicKey: ByteArray, signature: ByteArray, message: ByteArray): Boolean =
        Ed25519JNI().verify(publicKey, signature, message)
}