package com.smallraw.chain.bitcoincore.addressConvert

import com.smallraw.chain.bitcoincore.PublicKey
import com.smallraw.chain.bitcoincore.address.Address
import com.smallraw.chain.bitcoincore.execptions.BitcoinException
import com.smallraw.chain.bitcoincore.script.Script
import com.smallraw.chain.bitcoincore.script.ScriptType

interface IAddressConverter {
    @Throws(BitcoinException.AddressFormatException::class)
    fun convert(addressString: String): Address

    @Throws(BitcoinException.AddressFormatException::class)
    fun convert(hashBytes: ByteArray, scriptType: ScriptType = ScriptType.P2PKH): Address

    @Throws(BitcoinException.AddressFormatException::class)
    fun convert(publicKey: PublicKey, scriptType: ScriptType = ScriptType.P2PKH): Address

    @Throws(BitcoinException.AddressFormatException::class)
    fun convert(
        script: Script,
        scriptType: ScriptType = ScriptType.P2SH
    ): Address
}