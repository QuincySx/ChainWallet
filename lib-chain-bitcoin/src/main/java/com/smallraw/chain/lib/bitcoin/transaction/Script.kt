package com.smallraw.chain.lib.bitcoin.transaction

import com.smallraw.chain.lib.bitcoin.stream.BitcoinOutputStream
import com.smallraw.chain.lib.extensions.toHex
import java.io.ByteArrayOutputStream
import java.util.*

interface ScriptElement {
    fun getValue(): ByteArray

    fun getSize(): Int

    override fun toString(): String
}

class ScriptValue(private val value: ByteArray) : ScriptElement {
    override fun getValue(): ByteArray {
        return value
    }

    override fun getSize(): Int {
        return value.size
    }

    override fun toString(): String {
        return value.toHex()
    }
}

class ScriptOperator(private val op: Byte) : ScriptElement {
    companion object {
        const val OP_FALSE: Byte = 0
        const val OP_TRUE: Byte = 0x51
        const val OP_PUSHDATA1: Byte = 0x4c
        const val OP_PUSHDATA2: Byte = 0x4d
        const val OP_PUSHDATA4: Byte = 0x4e
        const val OP_DUP: Byte = 0x76 //Duplicates the top stack item.
        const val OP_DROP: Byte = 0x75
        const val OP_HASH160 = 0xA9.toByte() //The input is hashed twice: first with

        // SHA-256 and then with RIPEMD-160.
        const val OP_VERIFY: Byte = 0x69 //Marks transaction as invalid if top stack

        // value is not true. True is removed, but false is not.
        const val OP_EQUAL = 0x87.toByte() //Returns 1 if the inputs are exactly

        // equal, 0 otherwise.
        const val OP_EQUALVERIFY = 0x88.toByte() //Same as OP_EQUAL, but runs

        // OP_VERIFY afterward.
        const val OP_CHECKSIG = 0xAC.toByte() //The entire transaction's outputs,

        // inputs, and script (from the most recently-executed OP_CODESEPARATOR to the end) are
        // hashed. The signature used by OP_CHECKSIG must be a valid signature for this hash and
        // public key. If it is, 1 is returned, 0 otherwise.
        const val OP_CHECKSIGVERIFY = 0xAD.toByte()
        const val OP_NOP: Byte = 0x61
        const val OP_RETURN: Byte = 0x6a
        const val SIGHASH_ALL: Byte = 1
    }

    override fun getValue(): ByteArray {
        return byteArrayOf(op)
    }

    override fun getSize(): Int {
        return 1
    }

    override fun toString(): String {
        return when (op) {
            OP_NOP -> "OP_NOP"
            OP_DROP -> "OP_DROP"
            OP_DUP -> "OP_DUP"
            OP_HASH160 -> "OP_HASH160"
            OP_EQUAL -> "OP_EQUAL"
            OP_EQUALVERIFY -> "OP_EQUALVERIFY"
            OP_VERIFY -> "OP_VERIFY"
            OP_CHECKSIG -> "OP_CHECKSIG"
            OP_CHECKSIGVERIFY -> "OP_CHECKSIGVERIFY"
            OP_FALSE -> "OP_FALSE"
            OP_TRUE -> "OP_TRUE"
            OP_PUSHDATA1 -> "OP_PUSHDATA1"
            OP_PUSHDATA2 -> "OP_PUSHDATA2"
            OP_PUSHDATA4 -> "OP_PUSHDATA4"
            OP_RETURN -> "OP_RETURN"
            SIGHASH_ALL -> "SIGHASH_ALL"
            else -> " "
        }
    }
}

class OP {
    companion object {
        val OP_FALSE = ScriptOperator(ScriptOperator.OP_FALSE)
        val OP_TRUE = ScriptOperator(ScriptOperator.OP_TRUE)
        val OP_PUSHDATA1 = ScriptOperator(ScriptOperator.OP_PUSHDATA1)
        val OP_PUSHDATA2 = ScriptOperator(ScriptOperator.OP_PUSHDATA2)
        val OP_PUSHDATA4 = ScriptOperator(ScriptOperator.OP_PUSHDATA4)
        val OP_DUP = ScriptOperator(ScriptOperator.OP_DUP)
        val OP_DROP = ScriptOperator(ScriptOperator.OP_DROP)
        val OP_HASH160 = ScriptOperator(ScriptOperator.OP_HASH160)
        val OP_VERIFY = ScriptOperator(ScriptOperator.OP_VERIFY)
        val OP_EQUAL = ScriptOperator(ScriptOperator.OP_EQUAL)
        val OP_EQUALVERIFY = ScriptOperator(ScriptOperator.OP_EQUALVERIFY)
        val OP_CHECKSIG = ScriptOperator(ScriptOperator.OP_CHECKSIG)
        val OP_CHECKSIGVERIFY = ScriptOperator(ScriptOperator.OP_CHECKSIGVERIFY)
        val OP_NOP = ScriptOperator(ScriptOperator.OP_NOP)
        val OP_RETURN = ScriptOperator(ScriptOperator.OP_RETURN)
        fun OP_VALUE(value: ByteArray) = ScriptValue(value)
    }
}

class Script(val bytes: ByteArray) {
    companion object {
        fun new(vararg elements: ScriptElement): Script {
            val outputStream = BitcoinOutputStream()
            elements.forEach {
                if (it is ScriptValue) {
                    outputStream.writeBytes(it.getValue())
                } else {
                    outputStream.write(it.getValue())
                }
            }
            return Script(outputStream.toByteArray())
        }
    }

    fun toByteArray(): ByteArray = bytes

    override fun toString(): String {
//        val stringBuffer = StringBuffer()
//        mScriptElements.forEach {
//            stringBuffer.append(it.toString())
//        }
//        return stringBuffer.toString()
        return ""
    }
}