package com.smallraw.chain.bitcoin.transaction.script


import com.smallraw.chain.lib.extensions.toHex
import com.smallraw.chain.bitcoin.utils.IntUtil
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

open class Script(val scriptBytes: ByteArray) {
    companion object {
        @JvmStatic
        fun scriptP2PKH(data: ByteArray): Script {
            return Script(
                OpCodes.p2pkhStart + OpCodes.push(data) + OpCodes.p2pkhEnd
            )
        }

        @JvmStatic
        protected fun isOP(chunk: ByteArray?, op: Int): Boolean {
            return chunk != null && chunk.size == 1 && chunk[0].toInt() and 0xFF == op
        }

        @JvmStatic
        protected fun isOP(chunk: Chunk?, op: Int): Boolean {
            val chunkBytes = chunk?.toBytes() ?: return false
            return chunkBytes.size == 1 && chunkBytes[0].toInt() and 0xFF == op
        }

        @JvmStatic
        fun scriptEncodeChunks(chunks: List<Chunk>): ByteArray {
            val outputStream = ByteArrayOutputStream()
            chunks.forEach { chunk ->
                outputStream.write(chunk.opcode.and(0xFF))
                chunk.data?.let { OpCodes.getDataLengthBytes(it) }
                outputStream.write(chunk.data)
            }
            return outputStream.toByteArray()
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
                        Chunk(opcode)
                    }
                    dataToRead > stream.available() -> {
                        return chunks
//                        throw Exception("Push of data element that is larger than remaining data")
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

    val chunks = try {
        parseChunks(scriptBytes)
    } catch (e: Exception) {
        e.printStackTrace()
        listOf()
    }

    class Chunk(val opcode: Int = OP_0, val data: ByteArray? = null) {
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

        fun toBytes(): ByteArray {
            val scriptOutputStream = ByteArrayOutputStream()
            scriptOutputStream.write(opcode.and(0xFF))
            data?.let { scriptOutputStream.write(it) }
            return scriptOutputStream.toByteArray()
        }

        override fun toString(): String {
            val buf = StringBuilder()
            //  opcode is a single byte of non-pushdata content
            if (opcode != null) {
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
            }

            return buf.toString()
        }
    }

    override fun toString() = chunks.joinToString(" ")
}

fun List<Script.Chunk>.toBytes(): ByteArray {
    return Script.scriptEncodeChunks(this)
}