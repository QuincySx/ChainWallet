package com.smallraw.chain.wallet.data.bean

import com.smallraw.chain.wallet.App

interface IChain {
    fun getId(): Long?
    fun getName(): String
    fun getChainType(): App.ChainType
    fun getBip44Index(): Int
    fun getUnitName(): String
    fun getUnitSymbol(): String
    fun getUnitDecimals(): Int
}

data class BitcoinChain(
    private var id: Long? = null,
    private var name: String,
    private var chainType: App.ChainType = App.ChainType.BTC,
    private var bip44Index: Int = 0,
    private val currencyName: String = "",
    private val currencySymbol: String = "",
    private val currencyDecimals: Int = 0,
) : IChain {
    override fun getId() = id
    override fun getName() = name
    override fun getChainType() = App.ChainType.BTC
    override fun getBip44Index() = bip44Index
    override fun getUnitName() = currencyName
    override fun getUnitSymbol() = currencySymbol
    override fun getUnitDecimals() = currencyDecimals
}

data class EthereumChain(
    private var id: Long? = null,
    val chainId: Long,
    private var name: String,
    private var chainType: App.ChainType = App.ChainType.BTC,
    private var bip44Index: Int = 0,
    private val currencyName: String = "",
    private val currencySymbol: String = "",
    private val currencyDecimals: Int = 0,
) : IChain {
    override fun getId() = id
    override fun getName() = name
    override fun getChainType() = App.ChainType.BTC
    override fun getBip44Index() = bip44Index
    override fun getUnitName() = currencyName
    override fun getUnitSymbol() = currencySymbol
    override fun getUnitDecimals() = currencyDecimals
}