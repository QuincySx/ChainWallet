package com.smallraw.chain.lib.bitcoin.network

import com.smallraw.chain.lib.bitcoin.network.Network

class TestNet : Network() {
    override var bip32HeaderPub: Int = 0x043587CF
    override var bip32HeaderPriv: Int = 0x04358394
    override var addressVersion: Int = 111
    override var addressWifVersion: Int = 239 //0xef
    override var addressSegwitHrp: String = "tb"
    override var addressScriptVersion: Int = 196
    override var coinType: Int = 1
}