package com.smallraw.chain.bitcoin.transaction.script


import com.smallraw.chain.bitcoin.stream.BitcoinOutputStream
import com.smallraw.chain.bitcoin.utils.IntUtil
import com.smallraw.chain.lib.extensions.toHex
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream


enum class ScriptType(val value: Int) {
    P2PKH(1),     // pay to pubkey hash (aka pay to address)
    P2PK(2),      // pay to pubkey
    P2SH(3),      // pay to script hash
    P2WPKH(4),    // pay to witness pubkey hash
    P2WSH(5),     // pay to witness script hash
    P2WPKHSH(6),  // P2WPKH nested in P2SH
    NULL_DATA(7),
    UNKNOWN(0);

    companion object {
        fun fromValue(value: Int): ScriptType? {
            return values().find { it.value == value }
        }
    }

    val isWitness: Boolean
        get() = this in arrayOf(P2WPKH, P2WSH, P2WPKHSH)
}

class ScriptParsingException : java.lang.Exception {
    constructor(script: ByteArray?) : super("Unable to parse script: " + script?.toHex()) {}
    constructor(message: String?) : super(message) {}

    companion object {
        private const val serialVersionUID = 1L
    }
}

open class Script {
    companion object {

        @JvmStatic
        protected fun isOP(chunk: Chunk?, op: Int): Boolean {
            val chunkBytes = chunk?.toBytes() ?: return false
            return chunkBytes.size == 1 && chunkBytes[0].toInt() and 0xFF == op
        }

        @JvmStatic
        fun parseChunks(bytes: ByteArray): List<Chunk> {
            val chunks = mutableListOf<Chunk>()
            val stream = ByteArrayInputStream(bytes)

            while (stream.available() > 0) {
                var dataToRead: Long = -1

                val opcode = stream.read()
                when (opcode) {
                    in 0 until OP_PUSHDATA1 -> {
                        // Read some bytes of data, where how many is the opcode value itself.
                        dataToRead = opcode.toLong()
                    }
                    OP_PUSHDATA1 -> {
                        if (stream.available() < 1) throw Exception("Unexpected end of script")

                        dataToRead = stream.read().toLong()
                    }
                    OP_PUSHDATA2 -> {
                        // Read a short, then read that many bytes of data.
                        if (stream.available() < 2) throw Exception("Unexpected end of script")

                        dataToRead = IntUtil.readUint16FromStream(stream).toLong()
                    }
                    OP_PUSHDATA4 -> {
                        // Read a uint32, then read that many bytes of data.
                        // Though this is allowed, because its value cannot be > 520, it should never actually be used
                        if (stream.available() < 4) throw Exception("Unexpected end of script")
                        dataToRead = IntUtil.readUint32FromStream(stream)
                    }
                }

                val chunk = when {
                    dataToRead < 0 -> {
                        Chunk.of(opcode)
                    }
                    dataToRead > stream.available() -> {
                        throw Exception("Push of data element that is larger than remaining data")
                    }
                    else -> {
                        val data = ByteArray(dataToRead.toInt())
                        check(
                            dataToRead == 0L ||
                                    stream.read(data, 0, dataToRead.toInt()).toLong() == dataToRead
                        )
                        Chunk(opcode, data)
                    }
                }

                chunks.add(chunk)
            }

            return chunks
        }
    }

    val scriptBytes: ByteArray
    val chunks: List<Chunk>

    constructor(scriptBytes: ByteArray) {
        this.scriptBytes = scriptBytes
        chunks = try {
            parseChunks(scriptBytes)
        } catch (e: Exception) {
            e.printStackTrace()
            listOf()
        }
    }

    constructor(chunks: List<Chunk>) {
        this.scriptBytes = chunks.toScriptBytes()
        this.chunks = chunks
    }

    class Chunk(val opcode: Int, val data: ByteArray? = null) {
        companion object {
            @JvmStatic
            fun of(opcode: Int): Chunk {
                return Chunk(opcode)
            }

            @JvmStatic
            fun of(data: ByteArray): Chunk {
                val byte = when (data.size) {
                    in 0x4c..0xff -> OP_PUSHDATA1
                    in 0x0100..0xffff -> {
                        OP_PUSHDATA2
                    }
                    in 0x10000..0xffffffff -> {
                        OP_PUSHDATA2
                    }
                    else -> OP_0
                }
                return Chunk(byte, data)
            }
        }

        /**
         * If this chunk is a single byte of non-pushdata content (could be OP_RESERVED or some invalid Opcode)
         */
        fun isOpCode(): Boolean {
            return opcode > OP_PUSHDATA4
        }

        /**
         * 转换为脚本参数块的 bytes
         * @return bytes
         */
        fun toBytes(): ByteArray {
            return if (isOpCode()) {
                byteArrayOf(opcode.toByte())
            } else if (data != null) {
                data
            } else {
                byteArrayOf(opcode.toByte()) // smallNum
            }
        }

        override fun toString(): String {
            val buf = StringBuilder()
            //  opcode is a single byte of non-pushdata content
            when {
                opcode > OP_PUSHDATA4 -> {
                    buf.append(OpCodes.getOpCodeName(opcode))
                }
                data != null -> {
                    // Data chunk
                    buf.append(OpCodes.getPushDataName(opcode))
                        .append("[")
                        .append(data.toHex())
                        .append("]")
                }
                else -> {
                    // Small num
                    buf.append(opcode)
                }
            }

            return buf.toString()
        }
    }

    override fun toString() = chunks.joinToString(" ")
}

fun List<Script.Chunk>.toScriptBytes(): ByteArray {
    val stream = BitcoinOutputStream()
    this.forEach { chunk ->
        if (chunk.isOpCode()) {
            stream.write(chunk.opcode);
        } else if (chunk.data != null) {
            stream.writeBytes(chunk.data)
        } else {
            stream.write(chunk.opcode) // smallNum
        }
    }
    return stream.toByteArray()
}