package com.smallraw.chain.bitcoin.convert

import com.smallraw.chain.bitcoin.Bitcoin
import com.smallraw.chain.bitcoin.transaction.script.ScriptType

interface IAddressConverter {
    @Throws
    fun convert(addressString: String): Bitcoin.Address

    @Throws
    fun convert(hash160Bytes: ByteArray, scriptType: ScriptType = ScriptType.P2PKH): Bitcoin.Address

    @Throws
    fun convert(
        publicKey: Bitcoin.PublicKey,
        scriptType: ScriptType = ScriptType.P2PKH
    ): Bitcoin.Address
}