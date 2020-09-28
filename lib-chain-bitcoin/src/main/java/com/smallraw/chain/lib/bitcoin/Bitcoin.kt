package com.smallraw.chain.lib.bitcoin

import com.smallraw.chain.lib.bitcoin.execptions.AddressFormatException
import com.smallraw.chain.lib.bitcoin.transaction.script.OpCodes
import com.smallraw.chain.lib.bitcoin.transaction.script.ScriptType
import com.smallraw.chain.lib.crypto.Base58

class Bitcoin {
    enum class AddressType {
        P2PKH,  // Pay to public key hash
        P2SH,   // Pay to script hash
        WITNESS // Pay to witness hash
    }

    //region Address
    open class Address(
        protected val address: String,
        val hashKey: ByteArray,
        val type: AddressType
    ) {
        val scriptType: ScriptType
            get() = when (type) {
                AddressType.P2PKH -> ScriptType.P2PKH
                AddressType.P2SH -> ScriptType.P2SH
                AddressType.WITNESS ->
                    if (hashKey.size == 20) ScriptType.P2WPKH else ScriptType.P2WSH
            }

        open val lockingScript: ByteArray
            get() = when (type) {
                AddressType.P2PKH -> OpCodes.p2pkhStart + OpCodes.push(hashKey) + OpCodes.p2pkhEnd
                AddressType.P2SH -> OpCodes.p2pshStart + OpCodes.push(hashKey) + OpCodes.p2pshEnd
                else -> throw AddressFormatException("Unknown Address Type")
            }

        fun getAddressBytes() = address

        fun getAddress() = Base58.decode(address)
    }
    //endregion


    //region PublicKey
    interface PublicKey {
        fun getHash(): ByteArray

        fun getKey(): ByteArray

        fun scriptHashP2WPKH(): ByteArray
    }
    //endregion

    //region PublicKey
    interface PrivateKey {
        fun getKey(): ByteArray

        fun getKeyFormat(): String
    }
    //endregion
}