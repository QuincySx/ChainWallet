package com.smallraw.chain.lib.jni

class Secp256k1JNI {
    external fun hexToBytes(hexString: String): ByteArray?
    external fun bytesToHex(bytes: ByteArray, size: Int): String?
    external fun createPrivateKey(): ByteArray?
    external fun createPublicKey(privateKey: ByteArray, compressed: Boolean): ByteArray?
    external fun sign(privateKey: ByteArray, message: ByteArray): ByteArray?
    external fun verify(publicKey: ByteArray, signature: ByteArray, message: ByteArray): Boolean

    companion object {
        init {
            System.loadLibrary("secp256k1-wrapper")
        }
    }
}