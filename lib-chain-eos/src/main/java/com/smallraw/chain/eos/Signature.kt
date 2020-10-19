package com.smallraw.chain.eos

import com.smallraw.crypto.core.crypto.Base58
import com.smallraw.crypto.core.crypto.Ripemd160

class Signature(private val byteArray: ByteArray) {
    // 获取签名就会复制一份，不会被修改
    fun signBytes() = byteArray.copyOf()

    fun signature(): String {
        val checksum = Ripemd160.ripemd160(byteArray + "K1".toByteArray()).copyOfRange(0, 4)
        return "SIG_K1_" + Base58.encode(byteArray + checksum)
    }
}