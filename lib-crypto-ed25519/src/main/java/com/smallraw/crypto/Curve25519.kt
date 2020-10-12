package com.smallraw.crypto

import com.smallraw.crypto.core.execptions.JNICallException
import com.smallraw.crypto.core.random.RandomGenerator
import com.smallraw.crypto.jni.Ed25519JNI

object Curve25519 {

    private val random by lazy {
        RandomGenerator.getRandom()
    }

    fun createPrivateKey(): ByteArray {
        val bytes = ByteArray(32)
        random.nextBytes(bytes)
        return bytes
    }

    fun createPublicKey(privateKey: ByteArray): ByteArray =
        Ed25519JNI().curve25519CreatePublicKey(privateKey) ?: throw JNICallException()

    fun createShareKey(localPrivateKey: ByteArray, remotePublicKey: ByteArray): ByteArray =
        Ed25519JNI().curve25519CreateSharedKey(localPrivateKey, remotePublicKey)
            ?: throw JNICallException()

}