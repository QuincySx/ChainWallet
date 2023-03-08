package com.smallraw.chain.bitcoincore

open class Signature(private val byteArray: ByteArray) {
    // 获取签名就会复制一份，不会被修改
    open fun signature() = byteArray.copyOf()
}