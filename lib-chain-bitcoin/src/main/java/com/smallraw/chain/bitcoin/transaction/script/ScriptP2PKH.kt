package com.smallraw.chain.bitcoin.transaction.script

import com.smallraw.chain.bitcoin.Bitcoin
import com.smallraw.chain.lib.crypto.Base58
import com.smallraw.chain.lib.crypto.DEREncode
import com.smallraw.chain.bitcoin.network.BaseNetwork
import com.smallraw.chain.bitcoin.stream.ByteReader
import com.smallraw.chain.bitcoin.stream.ByteWriter
import java.io.EOFException

class ScriptInputP2PKH : ScriptInput {
    companion object {
        @Throws(ScriptParsingException::class)
        fun isScriptInputStandard(chunks: List<Chunk>): Boolean {
            return try {
                if (chunks.size != 2) {
                    return false
                }
                if (chunks[0].toBytes().size < 6) {
                    // not enough bytes to encode tag, and two lengths
                    return false
                }

                // Verify that first chunk contains two DER encoded BigIntegers
                val reader = ByteReader(chunks[0].toBytes())

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
                reader.skip(length1.toLong())

                // Read second type, must be 0x02
                if (reader.readByte() and 0xFF != 0x02) {
                    return false
                }

                // Read second length
                val length2 = reader.readByte() and 0xFF
                reader.skip(length2.toLong())

                // Validate that the lengths add up to the total
                if (2 + length1 + 2 + length2 != length) {
                    return false
                }

                // Make sure that we have a hash type at the end
                if (reader.available() !== 1) {
                    false
                } else true

                // XXX we may want to add more checks to verify public key length in
                // second chunk
            } catch (e: EOFException) {
                throw ScriptParsingException("Unable to parse " + ScriptInputP2PKH::class.java.getSimpleName())
            }
        }
    }

    private val _signature: ByteArray
    private val _publicKeyBytes: ByteArray

    constructor(signature: ByteArray, publicKeyBytes: ByteArray) : super(
        listOf(Chunk.of(signature), Chunk.of(publicKeyBytes)).toBytes()
    ) {
        _signature = signature
        _publicKeyBytes = publicKeyBytes
    }

    constructor(chunks: List<Chunk>, scriptBytes: ByteArray) : super(scriptBytes) {
        _signature = chunks[0].toBytes()
        _publicKeyBytes = chunks[1].toBytes()
    }

    /**
     * Get the signature of this input.
     */
    fun getSignature(): ByteArray? {
        return _signature
    }

    /**
     * Get the public key bytes of this input.
     *
     * @return The public key bytes of this input.
     */
    fun getPublicKeyBytes(): ByteArray? {
        return _publicKeyBytes
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
        return _signature[_signature.size - 1].toInt() and 0xFF
    }

    override fun getUnmalleableBytes(): ByteArray? {
        val bytes: ByteArray = DEREncode.sigToDer(_signature) ?: return null
        val writer = ByteWriter()
        writer.writeBytes(bytes.copyOfRange(0, 32))
        writer.writeBytes(bytes.copyOfRange(32, bytes.size))
        return writer.toByteArray()
    }
}

class ScriptOutputP2PKH : ScriptOutput {
    companion object {
        fun isScriptOutputP2PKH(chunks: List<Chunk>): Boolean {
            if (chunks.size != 5 && chunks.size != 6) {
                return false
            }
            if (!isOP(chunks[0], OP_DUP)) {
                return false
            }
            if (!isOP(chunks[1], OP_HASH160)) {
                return false
            }
            if (chunks[2].toBytes().size != 20) {
                return false
            }
            if (!isOP(chunks[3], OP_EQUALVERIFY)) {
                return false
            }
            if (!isOP(chunks[4], OP_CHECKSIG)) {
                return false
            }
            return if (chunks.size == 6 && !isOP(chunks[5], OP_NOP)) {
                // Variant that has a NOP at the end
                false
            } else true
        }
    }

    private val _addressBytes: ByteArray

    constructor(chunks: List<Chunk>, scriptBytes: ByteArray) : super(scriptBytes) {
        _addressBytes = chunks[2].toBytes()
    }

    constructor(addressBytes: ByteArray) : super(
        listOf(
            Chunk.of(OP_DUP),
            Chunk.of(OP_HASH160),
            Chunk.of(addressBytes),
            Chunk.of(OP_EQUALVERIFY),
            Chunk.of(OP_CHECKSIG)
        ).toBytes()
    ) {
        _addressBytes = addressBytes
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
        return _addressBytes
    }
}