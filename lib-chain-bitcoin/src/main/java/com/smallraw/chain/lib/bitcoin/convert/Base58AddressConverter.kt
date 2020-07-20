package com.smallraw.chain.lib.bitcoin.convert

import com.smallraw.chain.lib.bitcoin.AddressType
import com.smallraw.chain.lib.bitcoin.BitcoinAddress
import com.smallraw.chain.lib.bitcoin.BitcoinPublicKey
import com.smallraw.chain.lib.bitcoin.execptions.AddressFormatException
import com.smallraw.chain.lib.bitcoin.transaction.script.ScriptType
import com.smallraw.chain.lib.crypto.Base58
import com.smallraw.chain.lib.crypto.Sha256

class Base58AddressConverter(
    private val addressVersion: Int,
    private val addressScriptVersion: Int
) : IAddressConverter {

    override fun convert(addressString: String): BitcoinAddress {
        val data = Base58.decodeCheck(addressString)
        if (data.size != 20 + 1) {
            throw AddressFormatException("Address length is not 20 hash")
        }

        val bytes = data.copyOfRange(1, data.size)
        val addressType = when (data[0].toInt() and 0xFF) {
            addressScriptVersion -> AddressType.P2SH
            addressVersion -> AddressType.P2PKH
            else -> throw AddressFormatException("Wrong address prefix")
        }

        return BitcoinAddress(addressString, bytes, addressType)
    }

    override fun convert(bytes: ByteArray, scriptType: ScriptType): BitcoinAddress {
        val addressType: AddressType
        val addressVersion: Int

        when (scriptType) {
            ScriptType.P2PK,
            ScriptType.P2PKH -> {
                addressType = AddressType.P2PKH
                addressVersion = this.addressVersion
            }
            ScriptType.P2SH,
            ScriptType.P2WPKHSH -> {
                addressType = AddressType.P2SH
                addressVersion = addressScriptVersion
            }

            else -> throw AddressFormatException("Unknown Address Type")
        }

        val addressBytes = byteArrayOf(addressVersion.toByte()) + bytes

//        val addrChecksum = Sha256.doubleSha256(addressBytes).copyOfRange(0, 4)
//        val addressString = Base58.encode(addressBytes + addrChecksum)
        val addressString = Base58.encodeCheck(addressBytes)

        return BitcoinAddress(addressString, bytes, addressType)
    }

    override fun convert(publicKey: BitcoinPublicKey, scriptType: ScriptType): BitcoinAddress {
        var keyhash = publicKey.getHash()

//        if (scriptType == ScriptType.P2WPKHSH) {
//            keyhash = publicKey.scriptHashP2WPKH
//        }

        return convert(keyhash, scriptType)
    }
}
