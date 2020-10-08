package com.smallraw.chain.bitcoincore.address

import com.smallraw.chain.bitcoincore.script.Script

interface Address {
    enum class AddressType {
        P2PKH,  // Pay to public key hash
        P2SH,   // Pay to script hash
        P2WSHV0, // Pay to witness hash
        P2WPKHV0 // Pay to witness hash
    }

    /**
     * 获取地址的哈希
     * @return String
     */
    fun toHash(): ByteArray

    /**
     * 获取地址
     * @return String
     */
    override fun toString(): String

    /**
     * 获取地址类型
     * @return AddressType
     */
    fun getType(): AddressType

    /**
     * 获取地址的锁定脚本（可能不对）
     * @return Script
     */
    fun lockScript(): Script
}