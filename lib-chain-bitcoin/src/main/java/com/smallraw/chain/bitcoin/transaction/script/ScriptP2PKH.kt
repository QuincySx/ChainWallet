package com.smallraw.chain.bitcoin.transaction.script

import com.smallraw.chain.bitcoincore.address.Address
import com.smallraw.chain.bitcoincore.address.P2PKHAddress
import com.smallraw.chain.bitcoincore.execptions.ScriptParsingException
import com.smallraw.chain.bitcoincore.network.BaseNetwork
import com.smallraw.chain.bitcoincore.script.Chunk
import com.smallraw.chain.bitcoincore.script.OP_CHECKSIG
import com.smallraw.chain.bitcoincore.script.OP_DUP
import com.smallraw.chain.bitcoincore.script.OP_EQUALVERIFY
import com.smallraw.chain.bitcoincore.script.OP_HASH160
import com.smallraw.chain.bitcoincore.script.OP_NOP
import com.smallraw.chain.bitcoincore.script.ScriptChunk
import com.smallraw.chain.bitcoincore.script.isOP
import com.smallraw.chain.bitcoincore.stream.BitcoinInputStream
import com.smallraw.chain.bitcoincore.stream.BitcoinOutputStream
import com.smallraw.crypto.core.crypto.Base58
import com.smallraw.crypto.core.crypto.DEREncode
import java.io.EOFException

class ScriptInputP2PKH : ScriptInput {
    companion object {
        @Throws(ScriptParsingException::class)
        fun isScriptInputStandard(chunks: List<ScriptChunk>): Boolean {
            return try {
                if (chunks.size != 2) {
                    return false
                }
                if (chunks[0].toBytes().size < 6) {
                    // not enough bytes to encode tag, and two lengths
                    return false
                }

                // Verify that first chunk contains two DER encoded BigIntegers
                val reader =
                    BitcoinInputStream(chunks[0].toBytes())

                // Read tag, must be 0x30
                if (reader.readByte() and 0xFF != 0x30) {
                    return false
                }

                // Read total length as a byte, standard inputs never get longer than
                // this
                val length = reader.readByte() and 0xFF

                // Read first type, must be 0x02
                if (reader.readByte() and 0xFF != 0x02) {
                    return false
                }

                // Read first length
                val length1 = reader.readByte() and 0xFF
                reader.skip(length1)

                // Read second type, must be 0x02
                if (reader.readByte() and 0xFF != 0x02) {
                    return false
                }

                // Read second length
                val length2 = reader.readByte() and 0xFF
                reader.skip(length2)

                // Validate that the lengths add up to the total
                if (2 + length1 + 2 + length2 != length) {
                    return false
                }

                // Make sure that we have a hash type at the end
                if (reader.available() != 1) {
                    false
                } else true

                // XXX we may want to add more checks to verify public key length in
                // second chunk
            } catch (e: EOFException) {
                throw ScriptParsingException("Unable to parse " + ScriptInputP2PKH::class.java.getSimpleName())
            }
        }
    }

    private val signature: ByteArray
    private val publicKeyBytes: ByteArray

    constructor(signature: ByteArray, publicKeyBytes: ByteArray) : super(
        listOf(Chunk(signature), Chunk(publicKeyBytes))
    ) {
        this.signature = signature
        this.publicKeyBytes = publicKeyBytes
    }

    constructor(chunks: List<ScriptChunk>, scriptBytes: ByteArray) : super(scriptBytes) {
        signature = chunks[0].toBytes()
        publicKeyBytes = chunks[1].toBytes()
    }

    /**
     * Get the signature of this input.
     */
    fun getSignature(): ByteArray? {
        return signature
    }

    /**
     * Get the public key bytes of this input.
     *
     * @return The public key bytes of this input.
     */
    fun getPublicKeyBytes(): ByteArray? {
        return publicKeyBytes
    }

    /**
     * The hash type.
     *
     *
     * Look for SIGHASH_ALL, SIGHASH_NONE, SIGHASH_SINGLE, SIGHASH_ANYONECANPAY
     * in the reference client
     */
    fun getHashType(): Int {
        // hash type is the last byte of the signature
        return signature[signature.size - 1].toInt() and 0xFF
    }

    override fun getUnmalleableBytes(): ByteArray? {
        val bytes: ByteArray = DEREncode.sigToDer(signature) ?: return null
        val writer = BitcoinOutputStream()
        writer.writeBytes(bytes.copyOfRange(0, 32))
        writer.writeBytes(bytes.copyOfRange(32, bytes.size))
        return writer.toByteArray()
    }
}

class ScriptOutputP2PKH : ScriptOutput {
    companion object {
        fun isScriptOutputP2PKH(chunks: List<ScriptChunk>): Boolean {
            if (chunks.size != 5 && chunks.size != 6) {
                return false
            }
            if (!chunks[0].isOP(OP_DUP)) {
                return false
            }
            if (!chunks[1].isOP(OP_HASH160)) {
                return false
            }
            if (chunks[2].toBytes().size != 20) {
                return false
            }
            if (!chunks[3].isOP(OP_EQUALVERIFY)) {
                return false
            }
            if (!chunks[4].isOP(OP_CHECKSIG)) {
                return false
            }
            return if (chunks.size == 6 && !chunks[5].isOP(OP_NOP)) {
                // Variant that has a NOP at the end
                false
            } else true
        }
    }

    private val addressBytes: ByteArray

    constructor(chunks: List<ScriptChunk>, scriptBytes: ByteArray) : super(scriptBytes) {
        addressBytes = chunks[2].toBytes()
    }

    constructor(addressBytes: ByteArray) : super(
        listOf(
            Chunk(OP_DUP),
            Chunk(OP_HASH160),
            Chunk(addressBytes),
            Chunk(OP_EQUALVERIFY),
            Chunk(OP_CHECKSIG)
        )
    ) {
        this.addressBytes = addressBytes
    }

    override fun getAddress(network: BaseNetwork): Address {
        val addressBytes = byteArrayOf(network.addressVersion.toByte()) + getAddressBytes()
        return P2PKHAddress(
            addressBytes,
            network.addressVersion,
            Base58.encodeCheck(addressBytes),
        )
    }

    override fun getAddressBytes(): ByteArray {
        return addressBytes
    }
}