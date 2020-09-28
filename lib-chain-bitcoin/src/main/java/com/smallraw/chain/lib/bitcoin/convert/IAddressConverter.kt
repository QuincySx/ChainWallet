package com.smallraw.chain.lib.bitcoin.convert

import com.smallraw.chain.lib.bitcoin.transaction.script.ScriptType
import com.smallraw.chain.lib.bitcoin.Bitcoin

interface IAddressConverter {
    @Throws
    fun convert(addressString: String): Bitcoin.Address

    @Throws
    fun convert(bytes: ByteArray, scriptType: ScriptType = ScriptType.P2PKH): Bitcoin.Address

    @Throws
    fun convert(
        publicKey: Bitcoin.PublicKey,
        scriptType: ScriptType = ScriptType.P2PKH
    ): Bitcoin.Address
}