package com.smallraw.chain.lib.crypto

import com.smallraw.chain.lib.jni.Secp256k1JNI

object Secp256K1 {
    private val secp256k1Wrapper: Secp256k1JNI by lazy {
        Secp256k1JNI()
    }

    fun hexToBytes(hexString: String): ByteArray =
        secp256k1Wrapper.hexToBytes(hexString) ?: byteArrayOf()

    fun bytesToHex(bytes: ByteArray): String =
        secp256k1Wrapper.bytesToHex(bytes, bytes.size) ?: ""

    fun createPrivateKey(): ByteArray? = secp256k1Wrapper.createPrivateKey()

    fun createPublicKey(privateKey: ByteArray, compressed: Boolean): ByteArray? =
        secp256k1Wrapper.createPublicKey(privateKey, compressed)

    fun sign(privateKey: ByteArray, message: ByteArray): ByteArray? =
        secp256k1Wrapper.sign(privateKey, message)

    fun verify(publicKey: ByteArray, signature: ByteArray, message: ByteArray): Boolean =
        secp256k1Wrapper.verify(publicKey, signature, message)
}