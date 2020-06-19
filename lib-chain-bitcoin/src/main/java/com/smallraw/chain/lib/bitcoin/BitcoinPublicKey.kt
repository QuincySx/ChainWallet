package com.smallraw.chain.lib.bitcoin

import com.smallraw.chain.lib.Secp256k1PublicKey

interface BitcoinPublicKey {
    fun getHash(): ByteArray

    fun getFormat(): ByteArray

    fun getPublicKey(): ByteArray
}