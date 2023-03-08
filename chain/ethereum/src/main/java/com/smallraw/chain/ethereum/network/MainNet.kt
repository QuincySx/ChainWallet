package com.smallraw.chain.ethereum.network

import com.smallraw.crypto.core.extensions.hexToByteArray

class MainNet : BaseNetwork() {
    override val id: Long = 1
    override val genesisBlockHash: ByteArray =
        "d4e56740f876aef8c010b86a40d5f56745a118d0906a34e69aec8c0db1cb8fa3".hexToByteArray()
}