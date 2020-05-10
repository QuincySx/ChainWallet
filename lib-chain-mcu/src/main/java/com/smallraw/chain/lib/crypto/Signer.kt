package com.smallraw.chain.lib.crypto

import java.security.PrivateKey

interface Signer {
    fun sign(privateKey: PrivateKey, message: ByteArray): Signature
}