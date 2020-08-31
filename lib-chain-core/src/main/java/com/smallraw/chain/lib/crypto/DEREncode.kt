package com.smallraw.chain.lib.crypto

import com.smallraw.chain.lib.execptions.JNICallException
import com.smallraw.chain.lib.jni.CryptoJNI

object DEREncode {
    fun sigToDer(sign: ByteArray): ByteArray {
        return CryptoJNI.sig_to_der(sign, sign.size) ?: throw JNICallException()
    }

    fun derToSig(sign: ByteArray): ByteArray {
        return CryptoJNI.der_to_sig(sign, sign.size) ?: throw JNICallException()
    }
}