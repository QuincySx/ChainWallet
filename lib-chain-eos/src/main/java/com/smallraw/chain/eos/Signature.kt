package com.smallraw.chain.eos

class Signature(private val byteArray: ByteArray) {
    // 获取签名就会复制一份，不会被修改
    fun signature() = byteArray.copyOf()
}