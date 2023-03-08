package com.smallraw.chain.ethereum.abi

import com.smallraw.chain.ethereum.extensions.merge
import com.smallraw.chain.ethereum.solidity.IntType
import com.smallraw.chain.ethereum.solidity.SolidityType
import com.smallraw.crypto.core.crypto.Keccak

object Abi {
    fun encodeMethodId(methodName: String, params: Array<String>): ByteArray {
        val stringBuffer = StringBuffer()
        stringBuffer.append(methodName)
        stringBuffer.append(params.joinToString(separator = ",", prefix = "(", postfix = ")"))
        return Keccak.sha256(stringBuffer.toString().toByteArray()).copyOfRange(0, 4)
    }

    fun encodeParam(type: String, param: Any): ByteArray {
        return encodeArguments(arrayOf(type), arrayOf(param))
    }

    fun encodeParams(types: Array<String>, params: Array<Any>): ByteArray {
        return encodeArguments(types, params)
    }

    fun decodeParam(encoded: ByteArray, type: String): Any? {
        return decode(encoded, arrayOf(type))[0]
    }

    fun decodeParams(encoded: ByteArray, types: Array<String>): Array<Any?> {
        return decode(encoded, types)
    }

    private fun encodeArguments(types: Array<String>, args: Array<Any>): ByteArray {
        if (args.size > types.size) throw java.lang.RuntimeException("Too many arguments: " + args.size + " > " + types.size)
        val types = types.map { SolidityType.getType(it) }
        var staticSize = 0
        var dynamicCnt = 0
        // calculating static size and number of dynamic params
        for (i in args.indices) {
            val type = types[i]
            if (type.isDynamicType()) {
                dynamicCnt++
            }
            staticSize += type.getFixedSize()
        }
        val bb = arrayOfNulls<ByteArray>(args.size + dynamicCnt)
        var curDynamicPtr = staticSize
        var curDynamicCnt = 0
        for (i in 0 until args.size) {
            if (types[i].isDynamicType()) {
                val dynBB: ByteArray = types[i].encode(args[i])
                bb[i] = IntType.encodeInt(curDynamicPtr)
                bb[args.size + curDynamicCnt] = dynBB
                curDynamicCnt++
                curDynamicPtr += dynBB.size
            } else {
                bb[i] = types[i].encode(args[i])
            }
        }
        return bb.merge()
    }

    private fun decode(encoded: ByteArray, params: Array<String>): Array<Any?> {
        val types = params.map { SolidityType.getType(it) }
        val ret = arrayOfNulls<Any>(params.size)
        var off = 0
        for (i in params.indices) {
            if (types[i].isDynamicType()) {
                ret[i] = types[i].decode(encoded, IntType.decodeInt(encoded, off).toInt())
            } else {
                ret[i] = types[i].decode(encoded, off)
            }
            off += types[i].getFixedSize()
        }
        return ret
    }
}