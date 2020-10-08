package com.smallraw.chain.lib.core.crypto

import com.smallraw.chain.lib.core.execptions.JNICallException
import com.smallraw.chain.lib.core.jni.CryptoJNI

object DEREncode {
    fun sigToDer(sign: ByteArray): ByteArray {
//        return CryptoJNI().sig_to_der(sign, sign.size) ?: throw JNICallException()
        val der = CryptoJNI().sig_to_der(sign, sign.size) ?: throw JNICallException()
        val derSize = der.size
        var removeCount = 0
        for (index in derSize - 1 downTo 0) {
            if (der[index] == 0.toByte()) {
                removeCount++
            } else {
                break
            }
        }
        return der.copyOfRange(0, derSize - removeCount)
    }

    fun derToSig(sign: ByteArray): ByteArray {
        return CryptoJNI().der_to_sig(sign, sign.size) ?: throw JNICallException()
    }
}