package com.smallraw.chain.bitcoincore.transaction

import com.smallraw.chain.bitcoincore.script.OP_0
import com.smallraw.chain.bitcoincore.script.Script
import com.smallraw.chain.bitcoincore.script.ScriptChunk
import com.smallraw.chain.bitcoincore.transaction.serializers.TransactionSerializer
import com.smallraw.chain.lib.core.extensions.toHex
import java.util.*

open class Transaction(
    var inputs: Array<Input>,
    var outputs: Array<Output>,
    var version: Int = DEFAULT_TX_VERSION,
    var lockTime: Int = DEFAULT_TX_LOCKTIME
) {

    fun hasSegwit(): Boolean {
        inputs.filter { it.hasWitness() }
            .forEach { _ ->
                return true
            }
        return false
    }

    class InputWitness(pushCount: Int = 0) {
        private val stack = ArrayList<ScriptChunk>(Math.min(pushCount, MAX_INITIAL_ARRAY_LENGTH))

        fun stackCount() = stack.size

        fun setStack(i: Int, value: ScriptChunk) {
            while (i >= stack.size) {
                stack.add(ScriptChunk(OP_0))
            }
            stack[i] = value
        }

        fun addStack(value: ScriptChunk) {
            stack.add(value)
        }

        fun iterator(): MutableIterator<ScriptChunk> {
            return stack.iterator()
        }

        override fun toString(): String {
            return printAsJsonArray(stack.map { "\"${it.toBytes().toHex()}\"" }.toTypedArray())
        }

        companion object {
            @JvmStatic
            fun default() = InputWitness()

            const val MAX_INITIAL_ARRAY_LENGTH = 20
        }
    }

    class Input(
        val outPoint: OutPoint,
        var script: Script? = null,
        var sequence: Int = DEFAULT_TX_SEQUENCE,
        var witness: InputWitness = InputWitness.default()
    ) {
        constructor(
            hash: ByteArray,
            index: Int,
            script: Script? = null,
            sequence: Int = DEFAULT_TX_SEQUENCE,
            witness: InputWitness = InputWitness.default()
        ) : this(OutPoint(hash, index), script, sequence, witness)

        companion object {
            fun default() = Input(OutPoint.default())
        }

        fun hasWitness(): Boolean {
            return witness.stackCount() != 0
        }

        override fun toString(): String {
            return """
                {
                "outPoint":$outPoint,
                "script":"$script",
                "sequence":"$sequence",
                "witnesses":$witness
                }"""
        }
    }

    class OutPoint(
        val hash: ByteArray,
        val index: Int
    ) {
        companion object {
            fun default() = OutPoint(byteArrayOf(), 0)
        }

        override fun toString(): String {
            return """
                {
                "hash":"${hash.toHex()}",
                "index":"$index"
                }"""
        }

    }

    class Output(
        val value: Long,
        val script: Script?
    ) {
        companion object {
            fun default() = Output(0L, Script(byteArrayOf()))
        }

        override fun toString(): String {
            return """
                {
                "value":"${value * 1e-8}",
                "script":"$script"
                }"""
        }

    }

    override fun toString(): String {
        return """
            {
            "version":"$version",
            "lockTime":"$lockTime",
            "segwit":"${hasSegwit()}",
            "inputs":
            ${printAsJsonArray(inputs)},
            "outputs":
            ${printAsJsonArray(outputs)},
            }
            """.trimIndent()
    }

    /**
     * 采用序列化的方式 copy 交易
     */
    fun copy(): Transaction =
        TransactionSerializer.deserialize(TransactionSerializer.serialize(this))

    companion object {
        val NEGATIVE_SATOSHI = -1L

        val DEFAULT_TX_VERSION = 2
        val DEFAULT_TX_LOCKTIME = 0x00000000

        val EMPTY_TX_SEQUENCE = 0x00000000
        val DEFAULT_TX_SEQUENCE = 0xFFFFFFFF.toInt()
    }
}

private fun printAsJsonArray(a: Array<*>): String {
    if (a.isEmpty()) {
        return "[]"
    }
    val iMax = a.size - 1
    val sb = StringBuilder()
    sb.append('[')
    var i = 0
    while (true) {
        sb.append(a[i].toString())
        if (i == iMax) return sb.append(']').toString()
        sb.append(",\n")
        i++
    }
}