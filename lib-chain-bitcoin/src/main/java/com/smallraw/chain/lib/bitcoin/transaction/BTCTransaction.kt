/*
 The MIT License (MIT)

 Copyright (c) 2013 Valentin Konovalov

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.*/
package com.smallraw.chain.lib.bitcoin.transaction

import com.smallraw.chain.lib.bitcoin.execptions.BitcoinException
import com.smallraw.chain.lib.bitcoin.stream.BitcoinInputStream
import com.smallraw.chain.lib.bitcoin.stream.BitcoinOutputStream
import com.smallraw.chain.lib.extensions.toHex
import java.io.EOFException
import java.io.IOException

class BTCTransaction {
    val version: Int
    val inputs: Array<Input>
    val outputs: Array<Output>
    val lockTime: Int

    constructor(rawBytes: ByteArray?) {
        if (rawBytes == null) {
            throw BitcoinException(BitcoinException.ERR_NO_INPUT, "empty input")
        }
        var bais: BitcoinInputStream? = null
        try {
            bais = BitcoinInputStream(rawBytes)
            version = bais.readInt32()
            if (version != 1 && version != 2 && version != 3) {
                throw BitcoinException(
                    BitcoinException.ERR_UNSUPPORTED, "Unsupported TX " +
                            "version", version
                )
            }
            val inputsCount = bais.readVarInt().toInt()
            inputs = Array(inputsCount) { Input.default() }
            for (i in 0 until inputsCount) {
                val outPoint = OutPoint(
                    bais.readBytes(32).reversedArray(),
                    bais.readInt32()
                )
                val script = bais.readBytes(bais.readVarInt().toInt())
                val sequence = bais.readInt32()
                inputs[i] = Input(
                    outPoint,
                    Script(script),
                    sequence
                )
            }
            val outputsCount = bais.readVarInt().toInt()
            outputs = Array(outputsCount) { Output.default() }
            for (i in 0 until outputsCount) {
                val value = bais.readInt64()
                val scriptSize = bais.readVarInt()
                if (scriptSize < 0 || scriptSize > 10000000) {
                    throw BitcoinException(
                        BitcoinException.ERR_BAD_FORMAT, "Script size for " +
                                "output " + i +
                                " is strange (" + scriptSize + " bytes)."
                    )
                }
                val script = bais.readBytes(scriptSize.toInt())
                outputs[i] = Output(
                    value,
                    Script(script)
                )
            }
            lockTime = bais.readInt32()
        } catch (e: EOFException) {
            throw BitcoinException(BitcoinException.ERR_BAD_FORMAT, "TX incomplete")
        } catch (e: IOException) {
            throw IllegalArgumentException("Unable to read TX")
        } catch (e: Error) {
            throw IllegalArgumentException("Unable to read TX: $e")
        } finally {
            if (bais != null) {
                try {
                    bais.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    constructor(
        inputs: Array<Input>,
        outputs: Array<Output>,
        lockTime: Int = DEFAULT_TX_LOCKTIME
    ) {
        version = 2
        this.inputs = inputs
        this.outputs = outputs
        this.lockTime = lockTime
    }

    val bytes: ByteArray
        get() {
            val baos = BitcoinOutputStream()
            try {
                baos.writeInt32(version)
                baos.writeVarInt(inputs.size.toLong())
                for (input in inputs) {
                    baos.write(input!!.outPoint.hash.reversedArray())
                    baos.writeInt32(input!!.outPoint.index)
                    val scriptLen = if (input.script == null) 0 else input.script.bytes.size
                    baos.writeVarInt(scriptLen.toLong())
                    if (scriptLen > 0) {
                        baos.write(input.script!!.bytes)
                    }
                    baos.writeInt32(input.sequence)
                }
                baos.writeVarInt(outputs.size.toLong())
                for (output in outputs) {
                    baos.writeInt64(output!!.value)
                    val scriptLen = if (output.script == null) 0 else output.script.bytes.size
                    baos.writeVarInt(scriptLen.toLong())
                    if (scriptLen > 0) {
                        baos.write(output.script!!.bytes)
                    }
                }
                baos.writeInt32(lockTime)
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    baos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return baos.toByteArray()
        }

    override fun toString(): String {
        return """
            {
            "inputs":
            ${printAsJsonArray(inputs)},
            "outputs":
            ${printAsJsonArray(outputs)},
            "lockTime":"$lockTime"
            ,
            "version":"$version"}

            """.trimIndent()
    }

    private fun printAsJsonArray(a: Array<*>): String {
        if (a == null) {
            return "null"
        }
        if (a.size == 0) {
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
        ) : this(OutPoint(hash, index), script, sequence)

        companion object {
            fun default(): Input {
                return Input(OutPoint.default(), Script(byteArrayOf()), 0)
            }
        }

        override fun toString(): String {
            return """
                {
                "outPoint":$outPoint,
                "script":"$script",
                "sequence":"${Integer.toHexString(sequence)}"
                }

                """.trimIndent()
        }

    }

    class OutPoint // the transaction with the above hash (output number 2 = output index 1)
        (//32-byte hash of the transaction from which we want to redeem
        val hash: ByteArray, // an output
        val index //Four-byte field denoting the output index we want to redeem from
        : Int
    ) {
        companion object {
            fun default(): OutPoint {
                return OutPoint(byteArrayOf(), 0)
            }
        }

        override fun toString(): String {
            return "{" + "\"hash\":\"" + hash.toHex() + "\", \"index\":\"" + index + "\"}"
        }

    }

    class Output(
        val value: Long,
        val script: Script
    ) {
        companion object {
            fun default(): Output {
                return Output(0L, Script(byteArrayOf()))
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

    companion object {
        val DEFAULT_TX_LOCKTIME = 0x00000000

        val EMPTY_TX_SEQUENCE = 0x00000000
        val DEFAULT_TX_SEQUENCE = 0xFFFFFFFF.toInt()
    }
}