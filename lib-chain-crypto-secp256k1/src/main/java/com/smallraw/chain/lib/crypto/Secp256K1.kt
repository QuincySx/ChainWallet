package com.smallraw.chain.lib.crypto

import com.smallraw.chain.lib.jni.Secp256k1JNI
import java.security.SecureRandom

object Secp256K1 {
    private val secp256k1Wrapper: Secp256k1JNI by lazy {
        Secp256k1JNI()
    }

    private val random by lazy {
        // 安全但是慢
        // SecureRandom.getInstance("NativePRNG")
        // 没有 NativePRNG 安全但是快
        SecureRandom.getInstance("SHA1PRNG")
        // 默认规则
        // SecureRandom()
    }

    fun createPrivateKey(): ByteArray {
        return random.generateSeed(32)
    }

    fun createPublicKey(privateKey: ByteArray, compressed: Boolean): ByteArray? =
        secp256k1Wrapper.createPublicKey(privateKey, compressed)

    fun sign(privateKey: ByteArray, message: ByteArray): ByteArray? =
        secp256k1Wrapper.sign(privateKey, message)

    fun verify(publicKey: ByteArray, signature: ByteArray, message: ByteArray): Boolean =
        secp256k1Wrapper.verify(publicKey, signature, message)
}