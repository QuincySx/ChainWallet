package com.smallraw.chain.lib.jni

class CryptoJNI {
    companion object {
        init {
            System.loadLibrary("crypto-wrapper")
        }
    }

    external fun base58Encode(date: ByteArray, dataSize: Int = date.size): String?
    external fun sha256(date: ByteArray, dataSize: Int = date.size): ByteArray?
    external fun doubleSha256(date: ByteArray, dataSize: Int = date.size): ByteArray?
}