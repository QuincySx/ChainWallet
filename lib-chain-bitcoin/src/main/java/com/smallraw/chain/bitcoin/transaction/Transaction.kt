package com.smallraw.chain.bitcoin.transaction

import com.smallraw.chain.lib.Signature
import com.smallraw.chain.bitcoin.transaction.script.Script
import com.smallraw.chain.lib.extensions.toHex

open class Transaction(
    val version: Int = 2,
    val lockTime: Int = 0,
    val inputs: Array<Input>,
    val outputs: Array<Output>,
    val segwit: Boolean = false,
    val witnesses: Array<Signature> = arrayOf()
) {

    class Input {
        val outPoint: OutPoint
        val script: Script?
        val sequence: Int

        constructor(
            outPoint: OutPoint,
            script: Script? = null,
            sequence: Int = DEFAULT_TX_SEQUENCE
        ) {
            this.outPoint = outPoint
            this.script = script
            this.sequence = sequence
        }

        constructor(
            hash: ByteArray,
            index: Int,
            script: Script? = null,
            sequence: Int = DEFAULT_TX_SEQUENCE
        ) : this(
            OutPoint(
                hash,
                index
            ), script, sequence
        )

        companion object {
            fun default(): Input {
                return Input(
                    OutPoint.default(),
                    Script(byteArrayOf()), 0
                )
            }
        }

        override fun toString(): String {
            return """
                {
                "outPoint":$outPoint,
                "script":"$script",
                "sequence":"$sequence"
                }

                """.trimIndent()
        }

    }

    class OutPoint(
        val hash: ByteArray,
        val index: Int
    ) {
        companion object {
            fun default(): OutPoint {
                return OutPoint(
                    byteArrayOf(),
                    0
                )
            }
        }

        override fun toString(): String {
            return "{" + "\"hash\":\"" + hash.toHex() + "\", \"index\":\"" + index + "\"}"
        }

    }

    class Output(
        val value: Long,
        val script: Script?
    ) {
        companion object {
            fun default(): Output {
                return Output(
                    0L,
                    Script(byteArrayOf())
                )
            }
        }

        override fun toString(): String {
            return """
                {
                "value":"${value * 1e-8}","script":"$script"
                }
                """.trimIndent()
        }

    }

    override fun toString(): String {
        return """
            {
            "inputs":
            ${printAsJsonArray(inputs)},
            "outputs":
            ${printAsJsonArray(outputs)},
            "lockTime":"$lockTime",
            "version":"$version",
            "segwit":"$segwit",
            "witnesses":"${printAsJsonArray(witnesses)}"}
            """.trimIndent()
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

    companion object {
        val DEFAULT_TX_LOCKTIME = 0x00000000

        val EMPTY_TX_SEQUENCE = 0x00000000
        val DEFAULT_TX_SEQUENCE = 0xFFFFFFFF.toInt()
    }
}