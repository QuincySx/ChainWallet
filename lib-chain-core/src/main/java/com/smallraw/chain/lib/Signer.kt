package com.smallraw.chain.lib

import com.smallraw.chain.lib.Signature
import java.security.PrivateKey

/**
 * 签名器
 */
interface Signer {
    fun sign(privateKey: PrivateKey, message: ByteArray): Signature

    fun verify(publicKey: ByteArray, signature: ByteArray, message: ByteArray): Boolean
}