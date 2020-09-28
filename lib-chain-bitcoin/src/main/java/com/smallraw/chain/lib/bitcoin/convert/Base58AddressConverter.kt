package com.smallraw.chain.lib.bitcoin.convert

import com.smallraw.chain.lib.bitcoin.execptions.AddressFormatException
import com.smallraw.chain.lib.bitcoin.transaction.script.ScriptType
import com.smallraw.chain.lib.bitcoin.Bitcoin
import com.smallraw.chain.lib.crypto.Base58

class Base58AddressConverter(
    private val addressVersion: Int,
    private val addressScriptVersion: Int
) : IAddressConverter {

    override fun convert(addressString: String): Bitcoin.Address {
        val data = Base58.decodeCheck(addressString)
        if (data.size != 20 + 1) {
            throw AddressFormatException("Address length is not 20 hash")
        }

        val bytes = data.copyOfRange(1, data.size)
        val addressType = when (data[0].toInt() and 0xFF) {
            addressScriptVersion -> Bitcoin.AddressType.P2SH
            addressVersion -> Bitcoin.AddressType.P2PKH
            else -> throw AddressFormatException("Wrong address prefix")
        }

        return Bitcoin.Address(addressString, bytes, addressType)
    }

    override fun convert(bytes: ByteArray, scriptType: ScriptType): Bitcoin.Address {
        val addressType: Bitcoin.AddressType
        val addressVersion: Int

        when (scriptType) {
            ScriptType.P2PK,
            ScriptType.P2PKH -> {
                addressType = Bitcoin.AddressType.P2PKH
                addressVersion = this.addressVersion
            }
            ScriptType.P2SH,
            ScriptType.P2WPKHSH -> {
                addressType = Bitcoin.AddressType.P2SH
                addressVersion = addressScriptVersion
            }

            else -> throw AddressFormatException("Unknown Address Type")
        }

        val addressBytes = byteArrayOf(addressVersion.toByte()) + bytes

        val addressString = Base58.encodeCheck(addressBytes)

        return Bitcoin.Address(addressString, bytes, addressType)
    }

    override fun convert(publicKey: Bitcoin.PublicKey, scriptType: ScriptType): Bitcoin.Address {
        var keyhash = publicKey.getHash()

        if (scriptType == ScriptType.P2WPKHSH) {
            keyhash = publicKey.scriptHashP2WPKH()
        }

        return convert(keyhash, scriptType)
    }
}
