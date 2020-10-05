package com.smallraw.chain.bitcoin.convert

import com.smallraw.chain.bitcoin.Bitcoin
import com.smallraw.chain.bitcoin.crypto.Bech32Segwit
import com.smallraw.chain.bitcoin.execptions.AddressFormatException
import com.smallraw.chain.bitcoin.transaction.script.OpCodes
import com.smallraw.chain.bitcoin.transaction.script.Script
import com.smallraw.chain.bitcoin.transaction.script.ScriptType

abstract class Bech32AddressConverter(var hrp: String) : IAddressConverter

class SegwitAddressConverter(addressSegwitHrp: String) : Bech32AddressConverter(addressSegwitHrp) {
    override fun convert(addressString: String): Bitcoin.Address {
        val decoded = Bech32Segwit.decode(addressString)
        if (decoded.hrp != hrp) {
            throw AddressFormatException("Address HRP ${decoded.hrp} is not correct")
        }

        val payload = decoded.data
        val string = Bech32Segwit.encode(hrp, payload)
        val program = Bech32Segwit.convertBits(payload, 1, payload.size - 1, 5, 8, false)

        return Bitcoin.SegWitAddress(string, program, Bitcoin.Address.AddressType.WITNESS, 0)
    }

    override fun convert(hash160Bytes: ByteArray, scriptType: ScriptType): Bitcoin.Address {
        val addressType = when (scriptType) {
            ScriptType.P2WPKH -> Bitcoin.Address.AddressType.WITNESS
            ScriptType.P2WSH -> Bitcoin.Address.AddressType.WITNESS
            else -> throw AddressFormatException("Unknown Address Type")
        }

        val script = Script(hash160Bytes)
        val version = witnessVersion(script.chunks[0].opcode)
        val keyHash = script.chunks[1].data
        if (keyHash == null || version == null) {
            throw AddressFormatException("Invalid address size")
        }

        val witnessScript = Bech32Segwit.convertBits(keyHash, 0, keyHash.size, 8, 5, true)
        val addressString = Bech32Segwit.encode(hrp, byteArrayOf(version.toByte()) + witnessScript)

        return Bitcoin.SegWitAddress(addressString, keyHash, addressType, version)
    }

    override fun convert(publicKey: Bitcoin.PublicKey, scriptType: ScriptType): Bitcoin.Address {
        TODO("publicKeyHash")
//        val keyHash = OpCodes.scriptWPKH(publicKey.publicKeyHash)
//        return convert(keyHash, scriptType)
    }

    private fun witnessVersion(opcode: Int): Int? {
        //  OP_0 is encoded as 0x00
        if (opcode == 0) {
            return opcode
        }

        //  OP_1 through OP_16 are encoded as 0x51 though 0x60
        val version = opcode - 0x50
        if (version in 1..16) {
            return version
        }

        return null
    }
}