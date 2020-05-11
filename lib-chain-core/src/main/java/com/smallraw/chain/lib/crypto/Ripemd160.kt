package com.smallraw.chain.lib.crypto

import com.smallraw.chain.lib.jni.CryptoJNI

object Ripemd160 {
    fun hash(byteArray: ByteArray) = CryptoJNI.ripemd160(byteArray, byteArray.size)
}