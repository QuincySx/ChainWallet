package com.smallraw.chain.bitcoincore.script

import com.smallraw.chain.bitcoincore.stream.BitcoinOutputStream
import com.smallraw.chain.lib.core.extensions.toHex

fun List<ScriptChunk>.toScriptBytes(): ByteArray {
    val stream = BitcoinOutputStream(256)
    this.forEach { chunk ->
        if (chunk.isOpCode()) {
            stream.writeByte(chunk.opcode);
        } else if (chunk.data != null) {
            stream.writeScriptBytes(chunk.data)
        } else {
            stream.writeByte(chunk.opcode) // smallNum
        }
    }
    return stream.toByteArray()
}

fun Chunk(option: Int): ScriptChunk {
    return ScriptChunk.ofInt(option)
}

fun Chunk(option: ByteArray): ScriptChunk {
    return ScriptChunk.of(option)
}

fun Chunk(option: Byte): ScriptChunk {
    return ScriptChunk.of(option)
}

fun ScriptChunk?.isOP(op: Byte): Boolean {
    val chunkBytes = this?.toBytes() ?: return false
    return chunkBytes.size == 1 && chunkBytes[0] == op
}

/**
 * @param opcode OP_X 的比特币脚本参数
 * @param data 脚本数据，如果有 data 有数据，则 opcode 只能为 OP_PUSHDATA 的脚本参数，如果没有达到 OP_PUSHDATA1 的长度则 opcode 为 OP_0
 */
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
            if (data.size == 1) {
                return ScriptChunk(data[0])
            }

            val byte = when (data.size) {
                in 0x4c..0xff -> OP_PUSHDATA1
                in 0x0100..0xffff -> {
                    OP_PUSHDATA2
                }
                in 0x10000..0xffffffff -> {
                    OP_PUSHDATA4
                }
                // 没有 OP_PUSHDATA
                else -> OP_0
            }
            return ScriptChunk(byte, data)
        }
    }

    /**
     * If this chunk is a single byte of non-pushdata content (could be OP_RESERVED or some invalid Opcode)
     */
    fun isOpCode(): Boolean {
        return opcode.toInt().and(0xFF) > OP_PUSHDATA4
    }

    /**
     * 转换为脚本参数块的 bytes
     * @return bytes
     */
    fun toBytes(): ByteArray {
        return if (isOpCode()) {
            byteArrayOf(opcode)
        } else {
            data ?: byteArrayOf(opcode)
        } // smallNum
    }

    override fun toString(): String {
        val buf = StringBuilder()
        //  opcode is a single byte of non-pushdata content
        when {
            isOpCode() -> {
                buf.append(OpCodes.getOpCodeName(opcode))
            }
            data != null -> {
                // Data chunk
                buf.append(OpCodes.getPushDataName(opcode))
                    .append("<")
                    .append(data.toHex())
                    .append(">")
            }
            else -> {
                // Small num
                buf.append(opcode.toInt().and(0xFF))
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