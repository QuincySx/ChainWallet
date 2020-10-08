package com.smallraw.chain.bitcoin.transaction.script

import com.smallraw.chain.bitcoin.Bitcoin
import com.smallraw.chain.bitcoin.network.BaseNetwork
import com.smallraw.chain.bitcoin.stream.BitcoinInputStream
import com.smallraw.chain.bitcoin.stream.BitcoinOutputStream
import com.smallraw.chain.lib.core.crypto.Base58
import com.smallraw.chain.lib.core.crypto.DEREncode
import com.smallraw.chain.lib.core.crypto.Ripemd160
import java.io.EOFException

class ScriptInputP2PK : ScriptInput {
    companion object {
        @Throws(ScriptParsingException::class)
        fun isScriptInputP2PK(chunks: List<Chunk>): Boolean {
            return try {
                if (chunks.size != 1) {
                    return false
                }

                // Verify that the chunk contains two DER encoded BigIntegers
                val reader =
                    BitcoinInputStream(chunks[0].toBytes())

                // Read tag, must be 0x30
                if (reader.readByte() and 0xFF != 0x30) {
                    return false
                }

                // Read total length as a byte, standard inputs never get longer than
                // this
                val length = reader.readByte() as Int and 0xFF
                if (reader.available() < length) {
                    return false
                }

                // Read first type, must be 0x02
                if (reader.readByte() and 0xFF != 0x02) {
                    return false
                }

                // Read first length
                val length1 = reader.readByte() and 0xFF
                if (reader.available() < length1) {
                    return false
                }
                reader.skip(length1.toLong())

                // Read second type, must be 0x02
                if (reader.readByte() and 0xFF != 0x02) {
                    return false
                }

                // Read second length
                val length2 = reader.readByte() and 0xFF
                if (reader.available() < length2) {
                    return false
                }
                reader.skip(length2.toLong())

                // Make sure that we have 0x01 at the end
                if (reader.available() != 1) {
                    return false
                }
                if (reader.readByte() and 0xFF != 0x01) {
                    false
                } else true
            } catch (e: EOFException) {
                throw ScriptParsingException("Unable to parse " + ScriptInputP2PK::class.java.simpleName)
            }
        }
    }

    private val signature: ByteArray

    constructor(chunks: List<Chunk>, scriptBytes: ByteArray) : super(scriptBytes) {
        signature = chunks[0].toBytes()
    }

    constructor(signature: ByteArray) : super(
        listOf(Chunk.of(signature))
    ) {
        this.signature = signature
    }

    /**
     * Get the signature of this input.
     */
    fun getSignature(): ByteArray {
        return signature
    }

    override fun getUnmalleableBytes(): ByteArray? {
        val bytes: ByteArray = DEREncode.sigToDer(signature) ?: return null

        val writer = BitcoinOutputStream()
        writer.writeBytes(bytes.copyOfRange(0, 32))
        writer.writeBytes(bytes.copyOfRange(32, bytes.size))
        return writer.toByteArray()
    }
}

class ScriptOutputP2PK : ScriptOutput {
    companion object {
        fun isScriptOutputP2PK(chunks: List<Chunk>): Boolean {
            if (chunks.size != 2) {
                return false
            }
            return if (!isOP(chunks[1], OP_CHECKSIG)) {
                false
            } else true
        }
    }

    private val publicKeyBytes: ByteArray

    constructor(chunks: List<Chunk>, scriptBytes: ByteArray) : super(scriptBytes) {
        publicKeyBytes = chunks[0].toBytes()
    }

    constructor(publicKeyBytes: ByteArray) : super(
        listOf(
            Chunk.of(publicKeyBytes),
            Chunk.of(OP_CHECKSIG)
        )
    ) {
        this.publicKeyBytes = publicKeyBytes
    }

    /**
     * Get the public key bytes that this output is for.
     *
     * @return The public key bytes that this output is for.
     */
    fun getPublicKeyBytes(): ByteArray {
        return publicKeyBytes
    }

    override fun getAddress(network: BaseNetwork): Bitcoin.Address {
        val addressBytes = byteArrayOf(network.addressVersion.toByte()) + getAddressBytes()
        return Bitcoin.LegacyAddress(
            Base58.encodeCheck(addressBytes),
            addressBytes,
            Bitcoin.Address.AddressType.P2PKH
        )
    }

    override fun getAddressBytes(): ByteArray {
        return Ripemd160.hash160(getPublicKeyBytes())
    }
}