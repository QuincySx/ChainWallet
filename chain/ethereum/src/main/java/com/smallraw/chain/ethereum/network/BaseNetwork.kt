package com.smallraw.chain.ethereum.network

abstract class BaseNetwork {
    abstract val id: Long
    abstract val genesisBlockHash: ByteArray
}