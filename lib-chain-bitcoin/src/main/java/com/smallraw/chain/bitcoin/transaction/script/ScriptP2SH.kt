package com.smallraw.chain.bitcoin.transaction.script

import com.smallraw.chain.bitcoin.Bitcoin
import com.smallraw.chain.lib.crypto.Base58
import com.smallraw.chain.lib.crypto.DEREncode
import com.smallraw.chain.lib.crypto.Ripemd160
import com.smallraw.chain.bitcoin.network.BaseNetwork
import com.smallraw.chain.bitcoin.stream.ByteWriter

class ScriptInputP2SHMultisig : ScriptInput {
    companion object {
        @Throws(ScriptParsingException::class)
        fun isScriptInputP2SHMultisig(chunks: List<Chunk>): Boolean {
            if (chunks.size < 3) {
                return false
            }
            val scriptChunks: List<Chunk>
            try {
                scriptChunks = parseChunks(chunks[chunks.size - 1].toBytes())
            } catch (e: ScriptParsingException) {
                return false
            }
            if (scriptChunks.size < 4) {
                return false
            }
            //starts with an extra op cause of a bug in OP_CHECKMULTISIG
            if (!isOP(chunks[0], OP_0)) {
                return false
            }
            //last chunk in embedded script has to be CHECKMULTISIG
            if (!isOP(scriptChunks[scriptChunks.size - 1], OP_CHECKMULTISIG)) {
                return false
            }
            //first and second last chunk must have length 1, because they should be m and n values
            if (scriptChunks[0].toBytes().size != 1 || scriptChunks[scriptChunks.size - 2].toBytes().size != 1) {
                return false
            }
            //check for the m and n values
            try {
                val m: Int = OpCodes.opToIntValue(scriptChunks[0])
                val n: Int = OpCodes.opToIntValue(scriptChunks[scriptChunks.size - 2])
                if (n < 1 || n > 16) {
                    return false
                }
                if (m > n || m < 1 || m > 16) {
                    return false
                }
                //check that number of pubkeys matches n
                if (n != scriptChunks.size - 3) {
                    return false
                }
            } catch (ex: IllegalStateException) {
                //should hopefully not happen, since we check length before evaluating m and n
                //but its better to not risk BQS stopping in case something weird happens
                return false
            }
            return true
        }
    }

    private var _signatures: ArrayList<ByteArray>
    private var m = 0
    private var n = 0
    private var _pubKeys: ArrayList<ByteArray>
    private val _scriptHash: ByteArray
    private val _embeddedScript: Chunk

    constructor(chunks: List<Chunk>, scriptBytes: ByteArray) : super(scriptBytes) {
        //all but the first and last chunks are signatures, last chunk is the script
        _signatures = ArrayList(chunks.size - 1)
        _signatures.addAll(chunks.subList(1, chunks.size - 1).map { it.toBytes() })
        _embeddedScript = chunks[chunks.size - 1]
        val scriptChunks: List<Chunk> = parseChunks(_embeddedScript.toBytes())
        _scriptHash = Ripemd160.hash160(chunks[chunks.size - 1].toBytes())
        //the number of signatures needed
        m = OpCodes.opToIntValue(scriptChunks[0])
        //the total number of possible signing keys
        n = OpCodes.opToIntValue(scriptChunks[scriptChunks.size - 2])
        //collecting the pubkeys
        _pubKeys = ArrayList(n)
        _pubKeys.addAll(scriptChunks.subList(1, n + 1).map { it.toBytes() })
    }

    fun getPubKeys(): List<ByteArray?>? {
        return ArrayList(_pubKeys)
    }

    fun getSignatures(): List<ByteArray?>? {
        return ArrayList(_signatures)
    }

    fun getScriptHash(): ByteArray? {
        return _scriptHash.copyOf()
    }

    fun getEmbeddedScript(): ByteArray {
        return _embeddedScript.toBytes().copyOf()
    }

    fun getSigNumberNeeded(): Int {
        return m
    }

    override fun getUnmalleableBytes(): ByteArray? {
        val writer = ByteWriter()
        for (sig in _signatures) {
            val bytes: ByteArray = DEREncode.sigToDer(sig) ?: return null

            writer.writeBytes(bytes.copyOfRange(0, 32))
            writer.writeBytes(bytes.copyOfRange(32, bytes.size))
        }
        return writer.toByteArray()
    }
}

class ScriptOutputP2SH : ScriptOutput {
    companion object {
        fun isScriptOutputP2SH(chunks: List<Chunk>): Boolean {
            if (chunks.size != 3) {
                return false
            }
            if (!isOP(chunks[0], OP_HASH160)) {
                return false
            }
            if (chunks[1].toBytes().size != 20) {
                return false
            }
            return if (!isOP(chunks[2], OP_EQUAL)) {
                false
            } else true
        }
    }

    private val _p2shAddressBytes: ByteArray

    constructor(chunks: List<Chunk>, scriptBytes: ByteArray) : super(scriptBytes) {
        _p2shAddressBytes = chunks[1].toBytes()
    }

    constructor(addressBytes: ByteArray) : super(
        listOf(
            Chunk.of(OP_HASH160),
            Chunk.of(addressBytes),
            Chunk.of(OP_EQUAL)
        ).toBytes()
    ) {
        _p2shAddressBytes = addressBytes
    }

    override fun getAddress(network: BaseNetwork): Bitcoin.Address {
        val addressBytes = ByteArray(21)
        addressBytes[0] = (network.addressScriptVersion and 0xFF).toByte()
        System.arraycopy(_p2shAddressBytes, 0, addressBytes, 1, 20)
        return Bitcoin.LegacyAddress(
            Base58.encodeCheck(addressBytes),
            addressBytes,
            Bitcoin.Address.AddressType.P2SH
        )
    }

    override fun getAddressBytes() = _p2shAddressBytes
}