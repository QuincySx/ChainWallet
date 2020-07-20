package com.smallraw.chain.lib.bitcoin.network

class MainNet : Network() {
    override var bip32HeaderPub: Int =
        0x0488B21E   // The 4 byte header that serializes in base58 to "xpub".
    override var bip32HeaderPriv: Int =
        0x0488ADE4  // The 4 byte header that serializes in base58 to "xprv"
    override var addressVersion: Int = 0
    override var addressWifVersion: Int = 128 //0x80
    override var addressSegwitHrp: String = "bc"
    override var addressScriptVersion: Int = 5
    override var coinType: Int = 0
}