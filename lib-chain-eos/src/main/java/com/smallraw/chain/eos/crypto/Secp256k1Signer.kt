package com.smallraw.chain.eos.crypto

import com.smallraw.chain.eos.Signature
import com.smallraw.crypto.core.execptions.JNICallException
import com.smallraw.lib.crypto.Secp256K1
import java.security.PrivateKey

class Secp256k1Signer {
    @Throws(JNICallException::class)
    fun sign(privateKey: PrivateKey, message: ByteArray): Signature {
        val sign = Secp256K1.sign(privateKey.encoded, message)
        return Signature(sign)
    }

    @Throws(JNICallException::class)
    fun verify(publicKey: ByteArray, signature: ByteArray, message: ByteArray): Boolean {
        return Secp256K1.verify(publicKey, signature, message)
    }
}