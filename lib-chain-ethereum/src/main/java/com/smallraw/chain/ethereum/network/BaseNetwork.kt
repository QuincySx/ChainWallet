package com.smallraw.chain.ethereum.network

abstract class BaseNetwork {
    abstract val id: Int
    abstract val genesisBlockHash: ByteArray
}