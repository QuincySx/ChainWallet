package com.smallraw.chain.ethereum.crypto

import com.smallraw.chain.ethereum.Signature
import com.smallraw.crypto.core.execptions.JNICallException
import com.smallraw.lib.crypto.Secp256K1
import java.security.PrivateKey

class Secp256k1Signer {
    @Throws(JNICallException::class)
    fun sign(privateKey: PrivateKey, message: ByteArray): Signature {
        val ethSign = Secp256K1.ethSign(privateKey.encoded, message)
        val r = ethSign[0]?.copyOfRange(0, 32) ?: byteArrayOf()
        val s = ethSign[0]?.copyOfRange(32, 64) ?: byteArrayOf()
        val rce = ethSign[1]?.get(0)?.toInt() ?: 0
        return Signature(r, s, rce)
    }

    @Throws(JNICallException::class)
    fun verify(publicKey: ByteArray, signature: ByteArray, message: ByteArray): Boolean {
        return Secp256K1.verify(publicKey, signature, message)
    }
}