package com.smallraw.crypto.core.crypto

import com.smallraw.crypto.core.execptions.JNICallException
import com.smallraw.crypto.core.jni.CryptoJNI

object DEREncode {
    fun sigToDer(sign: ByteArray): ByteArray {
//        return CryptoJNI().sig_to_der(sign, sign.size) ?: throw JNICallException()
        return CryptoJNI().sig_to_der(sign, sign.size) ?: throw JNICallException()
    }

    fun derToSig(sign: ByteArray): ByteArray {
        return CryptoJNI().der_to_sig(sign, sign.size) ?: throw JNICallException()
    }
}