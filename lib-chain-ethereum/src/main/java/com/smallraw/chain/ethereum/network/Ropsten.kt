package com.smallraw.chain.ethereum.network

import com.smallraw.crypto.core.extensions.hexToByteArray

class Ropsten : BaseNetwork() {
    override val id: Int = 3
    override val genesisBlockHash: ByteArray =
        "41941023680923e0fe4d74a34bdac8141f2540e3ae90623718e47d66d1ca4a2d".hexToByteArray()
}