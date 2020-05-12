package com.smallraw.chain.lib.jni

class CryptoJNI {
    companion object {
        init {
            System.loadLibrary("crypto-wrapper")
        }
    }

    external fun base58EncodeCheck(date: ByteArray): String
}