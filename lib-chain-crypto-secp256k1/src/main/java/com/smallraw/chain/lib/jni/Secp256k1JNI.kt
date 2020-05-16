package com.smallraw.chain.lib.jni

class Secp256k1JNI {
    companion object {
        external fun createPublicKey(privateKey: ByteArray, compressed: Boolean): ByteArray?
        external fun sign(privateKey: ByteArray, message: ByteArray): ByteArray?
        external fun verify(publicKey: ByteArray, signature: ByteArray, message: ByteArray): Boolean

        init {
            System.loadLibrary("secp256k1-wrapper")
        }
    }
}