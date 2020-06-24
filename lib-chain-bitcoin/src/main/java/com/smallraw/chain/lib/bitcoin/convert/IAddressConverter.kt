package com.smallraw.chain.lib.bitcoin.convert

import com.smallraw.chain.lib.bitcoin.BitcoinAddress
import com.smallraw.chain.lib.bitcoin.BitcoinPublicKey
import com.smallraw.chain.lib.bitcoin.transaction.script.ScriptType

interface IAddressConverter {
    @Throws
    fun convert(addressString: String): BitcoinAddress

    @Throws
    fun convert(bytes: ByteArray, scriptType: ScriptType = ScriptType.P2PKH): BitcoinAddress

    @Throws
    fun convert(
        publicKey: BitcoinPublicKey,
        scriptType: ScriptType = ScriptType.P2PKH
    ): BitcoinAddress
}