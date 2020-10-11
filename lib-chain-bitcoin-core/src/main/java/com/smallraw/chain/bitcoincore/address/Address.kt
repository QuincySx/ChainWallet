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
     * 获取产生地址的哈希
     * @return HashKey
     */
    fun toHash(): ByteArray

    /**
     * 获取产生地址的哈希
     * @return HashKey
     */
    fun getHashKey(): ByteArray = toHash()

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
     * 一般名称为 lockScript
     * @return Script
     */
    fun scriptPubKey(): Script
}