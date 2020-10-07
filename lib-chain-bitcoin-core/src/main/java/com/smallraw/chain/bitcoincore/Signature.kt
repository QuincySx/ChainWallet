package com.smallraw.chain.bitcoincore

class Signature(private val byteArray: ByteArray) {
    open fun signature() = byteArray
}