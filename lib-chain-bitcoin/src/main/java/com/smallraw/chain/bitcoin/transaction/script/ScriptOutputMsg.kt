package com.smallraw.chain.bitcoin.transaction.script

import com.smallraw.chain.bitcoin.Bitcoin
import com.smallraw.chain.lib.crypto.Base58
import com.smallraw.chain.lib.crypto.Ripemd160
import com.smallraw.chain.bitcoin.network.BaseNetwork
import java.io.UnsupportedEncodingException

class ScriptOutputMsg : ScriptOutput {
    companion object {
        fun isScriptOutputMsg(chunks: List<Chunk>): Boolean {
            if (chunks.size != 4) {
                return false
            }
            if (!isOP(chunks[1], OP_DROP)) {
                return false
            }
            return if (!isOP(chunks[3], OP_CHECKSIG)) {
                false
            } else true
        }
    }

    private val _messageBytes: ByteArray
    private val _publicKeyBytes: ByteArray

    constructor(chunks: List<Chunk>, scriptBytes: ByteArray) : super(scriptBytes) {
        _messageBytes = chunks[0].toBytes()
        _publicKeyBytes = chunks[2].toBytes()
    }

    /**
     * Get the bytes for the message contained in this output.
     *
     * @return The message bytes of this output.
     */
    fun getMessageBytes(): ByteArray {
        return _messageBytes
    }

    fun getMessage(): String {
        return try {
            String(getMessageBytes(), Charsets.US_ASCII)
        } catch (e: UnsupportedEncodingException) {
            ""
        }
    }

    /**
     * Get the public key bytes that this output is for.
     *
     * @return The public key bytes that this output is for.
     */
    fun getPublicKeyBytes(): ByteArray {
        return _publicKeyBytes
    }

    override fun getAddressBytes(): ByteArray {
        return Ripemd160.hash160(getPublicKeyBytes())
    }

    override fun getAddress(network: BaseNetwork): Bitcoin.Address {
        val addressBytes = byteArrayOf(network.addressVersion.toByte()) + getAddressBytes()
        return Bitcoin.LegacyAddress(
            Base58.encodeCheck(addressBytes),
            addressBytes,
            Bitcoin.Address.AddressType.P2PKH
        )
    }
}