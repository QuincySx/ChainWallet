package com.smallraw.chain.bitcoincore.script

import com.smallraw.chain.bitcoincore.execptions.ScriptParsingException
import com.smallraw.chain.bitcoincore.stream.BitcoinInputStream

open class Script {
    companion object {
        @JvmStatic
        fun parseChunks(bytes: ByteArray): List<ScriptChunk> {
            val chunks = mutableListOf<ScriptChunk>()
            val stream = BitcoinInputStream(bytes)

            while (stream.available() > 0) {
                var dataToRead: Long = -1

                val opcode = stream.readByte()
                when (opcode) {
                    in 0 until OP_PUSHDATA1 -> {
                        // Read some bytes of data, where how many is the opcode value itself.
                        dataToRead = opcode.toLong()
                    }
                    OP_PUSHDATA1.toInt() -> {
                        if (stream.available() < 1) throw ScriptParsingException("Unexpected end of script")
                        dataToRead = stream.readByte().toLong()
                    }
                    OP_PUSHDATA2.toInt() -> {
                        // Read a short, then read that many bytes of data.
                        if (stream.available() < 2) throw ScriptParsingException("Unexpected end of script")
                        dataToRead = stream.readInt16().toLong()
                    }
                    OP_PUSHDATA4.toInt() -> {
                        // Read a uint32, then read that many bytes of data.
                        // Though this is allowed, because its value cannot be > 520, it should never actually be used
                        if (stream.available() < 4) throw ScriptParsingException("Unexpected end of script")
                        dataToRead = stream.readInt32().toLong()
                    }
                }

                val chunk = when {
                    dataToRead < 0 -> {
                        ScriptChunk.of(opcode.toByte())
                    }
                    dataToRead > stream.available() -> {
                        throw ScriptParsingException("Push of data element that is larger than remaining data")
                    }
                    else -> {
                        val data = ByteArray(dataToRead.toInt())
                        check(
                            dataToRead == 0L ||
                                    stream.readBytes(data, 0, dataToRead.toInt()).toLong() == dataToRead
                        )
                        ScriptChunk(opcode.toByte(), data)
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

    override fun toString() = chunks.joinToString(" ")
}