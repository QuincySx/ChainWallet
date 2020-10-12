package com.smallraw.chain.bitcoincore.addressConvert

import com.smallraw.chain.bitcoincore.PublicKey
import com.smallraw.chain.bitcoincore.address.Address
import com.smallraw.chain.bitcoincore.address.P2WPKHAddress
import com.smallraw.chain.bitcoincore.address.P2WSHAddress
import com.smallraw.chain.bitcoincore.crypto.Bech32Segwit
import com.smallraw.chain.bitcoincore.execptions.BitcoinException
import com.smallraw.chain.bitcoincore.script.OpCodes
import com.smallraw.chain.bitcoincore.script.Script
import com.smallraw.chain.bitcoincore.script.ScriptType
import com.smallraw.crypto.core.crypto.Sha256

abstract class Bech32AddressConverter(var hrp: String) : IAddressConverter

class SegwitAddressConverter(addressSegwitHrp: String) : Bech32AddressConverter(addressSegwitHrp) {
    override fun convert(addressString: String): Address {
        val decoded = Bech32Segwit.decode(addressString)
        if (decoded.hrp != hrp) {
            throw BitcoinException.AddressFormatException("Address HRP ${decoded.hrp} is not correct")
        }

        val payload = decoded.data
        val program = Bech32Segwit.convertBits(payload, 1, payload.size - 1, 5, 8, false)

        return if (program.size == 20) {
            P2WPKHAddress(program, hrp, addressString, OpCodes.intToOpCode(payload[0].toInt()))
        } else {
            P2WSHAddress(program, hrp, addressString, OpCodes.intToOpCode(payload[0].toInt()))
        }
    }

    override fun convert(hashBytes: ByteArray, scriptType: ScriptType): Address {
        return when (scriptType) {
            ScriptType.P2WPKH -> P2WPKHAddress(hashBytes, hrp, null)
            ScriptType.P2WSH -> P2WSHAddress(hashBytes, hrp, null)
            else -> throw BitcoinException.AddressFormatException("Unknown Address Type")
        }
    }

    override fun convert(publicKey: PublicKey, scriptType: ScriptType): Address {
        val keyHash = publicKey.getHash()
//        val script = Script(Chunk { OP_0 }, ChunkData { keyHash })
        return convert(keyHash, scriptType)
    }

    override fun convert(script: Script, scriptType: ScriptType): Address {
        if (scriptType != ScriptType.P2WSH) {
            throw BitcoinException.AddressFormatException("Unknown Address Type")
        }
        val keyHash = Sha256.sha256(script.scriptBytes)
        return convert(keyHash, scriptType)
    }
}