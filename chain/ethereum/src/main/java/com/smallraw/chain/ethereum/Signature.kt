package com.smallraw.chain.ethereum

class Signature(private val r: ByteArray, private val s: ByteArray, private val v: Int) {
    // 获取签名就会复制一份，不会被修改
    fun r() = r.copyOf()
    fun s() = s.copyOf()
    fun v() = v
}