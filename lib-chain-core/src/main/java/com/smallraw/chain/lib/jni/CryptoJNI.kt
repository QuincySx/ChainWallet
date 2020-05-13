package com.smallraw.chain.lib.jni

object CryptoJNI {
    init {
        System.loadLibrary("crypto-wrapper")
    }

    external fun base58Encode(date: ByteArray, dataSize: Int = date.size): String?
    external fun base58Decode(date: String): ByteArray?

    external fun sha256(date: ByteArray, dataSize: Int = date.size): ByteArray?
    external fun doubleSha256(date: ByteArray, dataSize: Int = date.size): ByteArray?
}