package com.smallraw.chain.bitcoincore.script

import com.smallraw.chain.bitcoincore.stream.BitcoinOutputStream
import com.smallraw.chain.lib.extensions.toHex
import java.util.*
import kotlin.collections.ArrayList

fun List<ScriptChunk>.toScriptBytes(): ByteArray {
    val stream = BitcoinOutputStream(256)
    this.forEach { chunk ->
        if (chunk.isOpCode()) {
            stream.writeByte(chunk.opcode);
        } else if (chunk.data != null) {
            stream.writeBytes(chunk.data)
        } else {
            stream.writeByte(chunk.opcode) // smallNum
        }
    }
    return stream.toByteArray()
}

fun ChunkInt(option: Int.() -> Int): ScriptChunk {
    var value = 0
    value = value.option()
    return ScriptChunk.ofInt(value)
}

fun Chunk(option: Byte.() -> Byte): ScriptChunk {
    var opcode = 0.toByte()
    opcode = opcode.option()
    return ScriptChunk.of(opcode)
}

fun ChunkData(option: ByteArray.() -> ByteArray): ScriptChunk {
    var data = byteArrayOf()
    data = data.option()
    return ScriptChunk.of(data)
}

fun ScriptChunk?.isOP(op: Byte): Boolean {
    val chunkBytes = this?.toBytes() ?: return false
    return chunkBytes.size == 1 && chunkBytes[0] == op
}

class ScriptChunk(val opcode: Byte, val data: ByteArray? = null) {
    companion object {
        @JvmStatic
        fun ofInt(value: Int): ScriptChunk {
            val intToOpCode = OpCodes.intToOpCode(value)
            return ScriptChunk(intToOpCode)
        }

        @JvmStatic
        fun of(opcode: Byte): ScriptChunk {
            return ScriptChunk(opcode)
        }

        @JvmStatic
        fun of(data: ByteArray): ScriptChunk {
            val byte = when (data.size) {
                in 0x4c..0xff -> OP_PUSHDATA1
                in 0x0100..0xffff -> {
                    OP_PUSHDATA2
                }
                in 0x10000..0xffffffff -> {
                    OP_PUSHDATA4
                }
                else -> OP_0
            }
            return ScriptChunk(byte, data)
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
            byteArrayOf(opcode)
        } else if (data != null) {
            data
        } else {
            byteArrayOf(opcode) // smallNum
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

operator fun List<ScriptChunk>.plus(chunk: ScriptChunk): List<ScriptChunk> {
    return this + listOf(chunk)
}

operator fun ScriptChunk.plus(chunks: List<ScriptChunk>): List<ScriptChunk> {
    return listOf(this) + chunks
}