package com.smallraw.chain.lib

import com.smallraw.chain.lib.Signature
import java.security.PrivateKey

interface Signer {
    fun sign(privateKey: PrivateKey, message: ByteArray): Signature
}