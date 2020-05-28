package com.smallraw.chain.lib.crypto

import com.smallraw.chain.lib.execptions.JNICallException
import com.smallraw.chain.lib.jni.Ed25519JNI
import com.smallraw.chain.lib.random.RandomGenerator

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
        Ed25519JNI.curve25519CreatePublicKey(privateKey) ?: throw JNICallException()

    fun createShareKey(localPrivateKey: ByteArray, remotePublicKey: ByteArray): ByteArray =
        Ed25519JNI.curve25519CreateSharedKey(localPrivateKey, remotePublicKey)
            ?: throw JNICallException()

}