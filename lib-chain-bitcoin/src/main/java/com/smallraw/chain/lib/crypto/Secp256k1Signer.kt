package com.smallraw.chain.lib.crypto

import com.smallraw.chain.lib.Signature
import com.smallraw.chain.lib.Signer
import com.smallraw.chain.lib.jni.Secp256k1JNI
import java.security.PrivateKey

class Secp256k1Signer : Signer {
    override fun sign(privateKey: PrivateKey, message: ByteArray): Signature {
        return Signature(Secp256K1.sign(privateKey.encoded, message))
    }

    override fun verify(publicKey: ByteArray, signature: ByteArray, message: ByteArray): Boolean {
        return Secp256K1.verify(publicKey, signature, message)
    }
}