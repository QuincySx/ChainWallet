package com.smallraw.chain.wallet.data.bean

import java.util.*

interface IWallet {
    fun getId(): Long?
    fun getAccount(): Account
    fun getChain(): IChain
    fun getWaleltName(): String
    fun getAddress(): String
    fun getDerivedPath(): String
    fun getCreateTime(): Date
}

data class BitcoinWallet(
    private val id: Long? = null,
    private var account: Account,
    private var chain: IChain,
    var name: String,
    private var address: String,
    private var derivedPath: String = "",
    private var createTime: Date? = null,
) : IWallet {
    override fun getId() = id
    override fun getAccount() = account
    override fun getChain() = chain
    override fun getWaleltName() = name
    override fun getAddress() = address
    override fun getDerivedPath() = derivedPath
    override fun getCreateTime() = createTime ?: Date()
}

data class EthereumWallet(
    private val id: Long? = null,
    private var account: Account,
    private var chain: IChain,
    var name: String,
    private var address: String,
    private var derivedPath: String = "",
    private var createTime: Date? = null,
) : IWallet {
    override fun getId() = id
    override fun getAccount() = account
    override fun getChain() = chain
    override fun getWaleltName() = name
    override fun getAddress() = address
    override fun getDerivedPath() = derivedPath
    override fun getCreateTime() = createTime ?: Date()
}