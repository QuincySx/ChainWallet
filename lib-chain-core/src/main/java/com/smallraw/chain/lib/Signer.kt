package com.smallraw.chain.lib

import java.security.PrivateKey

/**
 * 签名器
 */
interface Signer {
    fun sign(privateKey: PrivateKey, message: ByteArray): Signature
}