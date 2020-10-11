package com.smallraw.chain.bitcoin.transaction.script

import com.smallraw.chain.bitcoincore.address.Address
import com.smallraw.chain.bitcoincore.address.P2PKHAddress
import com.smallraw.chain.bitcoincore.network.BaseNetwork
import com.smallraw.chain.bitcoincore.script.OP_CHECKSIG
import com.smallraw.chain.bitcoincore.script.OP_DROP
import com.smallraw.chain.bitcoincore.script.ScriptChunk
import com.smallraw.chain.bitcoincore.script.isOP
import com.smallraw.chain.lib.core.crypto.Base58
import com.smallraw.chain.lib.core.crypto.Ripemd160
import java.io.UnsupportedEncodingException

class ScriptOutputMsg : ScriptOutput {
    companion object {
        fun isScriptOutputMsg(chunks: List<ScriptChunk>): Boolean {
            if (chunks.size != 4) {
                return false
            }
            if (!chunks[1].isOP(OP_DROP)) {
                return false
            }
            return if (!chunks[3].isOP(OP_CHECKSIG)) {
                false
            } else true
        }
    }

    private val messageBytes: ByteArray
    private val publicKeyBytes: ByteArray

    constructor(chunks: List<ScriptChunk>, scriptBytes: ByteArray) : super(scriptBytes) {
        messageBytes = chunks[0].toBytes()
        publicKeyBytes = chunks[2].toBytes()
    }

    /**
     * Get the bytes for the message contained in this output.
     *
     * @return The message bytes of this output.
     */
    fun getMessageBytes(): ByteArray {
        return messageBytes
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
        return publicKeyBytes
    }

    override fun getAddressBytes(): ByteArray {
        return Ripemd160.hash160(getPublicKeyBytes())
    }

    override fun getAddress(network: BaseNetwork): Address {
        val addressBytes = byteArrayOf(network.addressVersion.toByte()) + getAddressBytes()
        return P2PKHAddress(
            addressBytes,
            network.addressVersion,
            Base58.encodeCheck(addressBytes),
        )
    }
}