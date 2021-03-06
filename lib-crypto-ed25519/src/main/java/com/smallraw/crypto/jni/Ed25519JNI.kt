package com.smallraw.crypto.jni

class Ed25519JNI {
    companion object {
        init {
            System.loadLibrary("ed25519-wrapper")
        }
    }

    external fun createPublicKey(privateKey: ByteArray): ByteArray?
    external fun sign(
        privateKey: ByteArray,
        message: ByteArray,
        messageSize: Int = message.size
    ): ByteArray?

    external fun verify(
        publicKey: ByteArray,
        signature: ByteArray,
        message: ByteArray,
        messageSize: Int = message.size
    ): Boolean

    external fun curve25519CreatePublicKey(
        privateKey: ByteArray
    ): ByteArray?

    external fun curve25519CreateSharedKey(
        localPrivateKey: ByteArray,
        remotePublicKey: ByteArray
    ): ByteArray?

}