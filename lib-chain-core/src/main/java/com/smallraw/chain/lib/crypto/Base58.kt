package com.smallraw.chain.lib.crypto

import com.smallraw.chain.lib.jni.CryptoJNI

object Base58 {
    fun encode(byteArray: ByteArray): String? = CryptoJNI.base58Encode(byteArray, byteArray.size)
    fun decode(str: String): ByteArray? = CryptoJNI.base58Decode(str)
}