package com.smallraw.chain.bitcoincore.script

import com.smallraw.chain.bitcoincore.execptions.ScriptParsingException
import com.smallraw.chain.bitcoincore.stream.BitcoinInputStream
import com.smallraw.chain.lib.core.extensions.hexToByteArray

open class Script {
    companion object {
        @JvmStatic
        fun parseChunks(bytes: ByteArray): List<ScriptChunk> {
            val chunks = mutableListOf<ScriptChunk>()
            val stream = BitcoinInputStream(bytes)

            while (stream.available() > 0) {
                var opcode = stream.readByte()
                if (opcode >= 0xF0) {
                    opcode = (opcode shl 8) or stream.readByte()
                }

                val chunk = when (opcode) {
                    in 0 until OP_PUSHDATA1.toInt().and(0xFF) -> {
                        // Read some bytes of data, where how many is the opcode value itself.
                        ScriptChunk.of(stream.readBytes(opcode))
                    }
                    OP_PUSHDATA1.toInt().and(0xFF) -> {
                        if (stream.available() < 1) throw ScriptParsingException("Unexpected end of script")
                        val size = stream.readByte()
                        ScriptChunk.of(stream.readBytes(size))
                    }
                    OP_PUSHDATA2.toInt().and(0xFF) -> {
                        // Read a short, then read that many bytes of data.
                        if (stream.available() < 2) throw ScriptParsingException("Unexpected end of script")
                        val size = stream.readInt16()
                        ScriptChunk.of(stream.readBytes(size))
                    }
                    OP_PUSHDATA4.toInt().and(0xFF) -> {
                        // Read a uint32, then read that many bytes of data.
                        // Though this is allowed, because its value cannot be > 520, it should never actually be used
                        if (stream.available() < 4) throw ScriptParsingException("Unexpected end of script")
                        val size = stream.readInt32()
                        ScriptChunk.of(stream.readBytes(size))
                    }
                    else -> {
                        ScriptChunk.of(opcode.toByte())
                    }
                }
                chunks.add(chunk)
            }

            return chunks
        }
    }

    val scriptBytes: ByteArray
    val chunks: List<ScriptChunk>

    constructor(scriptBytes: ByteArray) {
        this.scriptBytes = scriptBytes
        chunks = try {
            parseChunks(scriptBytes)
        } catch (e: Exception) {
            e.printStackTrace()
            listOf()
        }
    }

    constructor(chunks: List<ScriptChunk>) {
        this.scriptBytes = chunks.toScriptBytes()
        this.chunks = chunks
    }

    constructor(vararg chunks: ScriptChunk) : this(chunks.toList())

    constructor(hexStr: String) : this(hexStr.hexToByteArray())

    override fun toString() = chunks.joinToString(" ")
}