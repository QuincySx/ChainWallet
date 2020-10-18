package com.smallraw.crypto.jni

class Secp256k1JNI {
    companion object {
        init {
            System.loadLibrary("secp256k1-wrapper")
        }
    }

    external fun createPublicKey(privateKey: ByteArray, compressed: Boolean): ByteArray?

    external fun sign(
        privateKey: ByteArray,
        message: ByteArray,
        messageSize: Int = message.size
    ): ByteArray?

    external fun ethSign(
        privateKey: ByteArray,
        message: ByteArray,
        messageSize: Int = message.size
    ): Array<ByteArray?>?

    external fun verify(
        publicKey: ByteArray,
        signature: ByteArray,
        message: ByteArray,
        messageSize: Int = message.size
    ): Boolean

}