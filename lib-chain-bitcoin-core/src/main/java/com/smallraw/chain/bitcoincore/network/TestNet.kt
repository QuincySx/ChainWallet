package com.smallraw.chain.bitcoincore.network

class TestNet : BaseNetwork() {
    override var bip32HeaderPub: Int = 0x043587CF
    override var bip32HeaderPriv: Int = 0x04358394
    override var addressVersion: Int = 111 //0x6F
    override var addressWifVersion: Int = 239 //0xEf
    override var addressSegwitHrp: String = "tb"
    override var addressScriptVersion: Int = 196 //0xC4
    override var coinType: Int = 1

    override val dustRelayTxFee = 3000
}