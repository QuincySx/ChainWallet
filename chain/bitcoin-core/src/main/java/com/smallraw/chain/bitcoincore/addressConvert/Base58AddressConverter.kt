package com.smallraw.chain.bitcoincore.addressConvert

import com.smallraw.chain.bitcoincore.PublicKey
import com.smallraw.chain.bitcoincore.address.Address
import com.smallraw.chain.bitcoincore.address.P2PKHAddress
import com.smallraw.chain.bitcoincore.address.P2SHAddress
import com.smallraw.chain.bitcoincore.execptions.BitcoinException
import com.smallraw.chain.bitcoincore.script.Chunk
import com.smallraw.chain.bitcoincore.script.OP_0
import com.smallraw.chain.bitcoincore.script.Script
import com.smallraw.chain.bitcoincore.script.ScriptType
import com.smallraw.crypto.core.crypto.Base58
import com.smallraw.crypto.core.crypto.Ripemd160

class Base58AddressConverter(
    private val addressVersion: Int,
    private val addressScriptVersion: Int
) : IAddressConverter {

    override fun convert(addressString: String): Address {
        val data = Base58.decodeCheck(addressString)
        if (data.size != 20 + 1) {
            throw BitcoinException.AddressFormatException("Address length is not 20 hash")
        }

        val bytes = data.copyOfRange(1, data.size)
        return when (data[0].toInt() and 0xFF) {
            addressScriptVersion -> P2SHAddress(bytes, addressString)
            addressVersion -> P2PKHAddress(bytes, addressString)
            else -> throw BitcoinException.AddressFormatException("Wrong address prefix")
        }
    }

    override fun convert(hashBytes: ByteArray, scriptType: ScriptType): Address {
        return when (scriptType) {
            ScriptType.P2PK,
            ScriptType.P2PKH -> {
                P2PKHAddress(hashBytes, this.addressVersion)
            }
            ScriptType.P2SH,
            ScriptType.P2SHWPKH,
            ScriptType.P2SHWSH -> {
                P2SHAddress(hashBytes, addressScriptVersion)
            }
            else -> throw BitcoinException.AddressFormatException("Unknown Address Type")
        }
    }

    override fun convert(publicKey: PublicKey, scriptType: ScriptType): Address {
        val keyHash = publicKey.getHash()

        if (scriptType == ScriptType.P2SHWPKH) {
            val script = Script(Chunk(OP_0), Chunk(keyHash))
            return convert(script, scriptType)
        }

        return convert(keyHash, scriptType)
    }

    override fun convert(script: Script, scriptType: ScriptType): Address {
        if (scriptType == ScriptType.P2SH ||
            scriptType == ScriptType.P2SHWSH ||
            scriptType == ScriptType.P2SHWPKH
        ) {
            val keyHash = Ripemd160.hash160(script.scriptBytes)
            return convert(keyHash, scriptType)
        }

        throw throw BitcoinException.AddressFormatException("Unknown Address Type")
    }
}
