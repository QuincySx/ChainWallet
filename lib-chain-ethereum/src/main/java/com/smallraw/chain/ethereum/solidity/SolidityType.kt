package com.smallraw.chain.ethereum.solidity

import com.smallraw.chain.ethereum.extensions.merge
import com.smallraw.crypto.core.extensions.toBigInteger
import com.smallraw.crypto.core.extensions.toBytes
import com.smallraw.crypto.core.extensions.toHex
import java.io.UnsupportedEncodingException
import java.math.BigInteger
import java.nio.charset.Charset
import java.util.*


abstract class SolidityType(protected var name: String) {
    companion object {
        fun getType(typeName: String): SolidityType {
            if (typeName.contains("[")) return ArrayType.getType(typeName)
            if ("bool" == typeName) return BoolType()
            if (typeName.startsWith("int") || typeName.startsWith("uint")) return IntType(
                typeName
            )
            if ("address" == typeName) return AddressType()
            if ("string" == typeName) return StringType()
            if ("bytes" == typeName) return BytesType()
            if ("function" == typeName) return FunctionType()
            if (typeName.startsWith("bytes")) return Bytes32Type(typeName)
            throw RuntimeException("Unknown type: $typeName")
        }
    }

    open fun getCanonicalName(): String {
        return name
    }

    abstract fun encode(value: Any): ByteArray

    abstract fun decode(encoded: ByteArray, offset: Int): Any

    fun decode(encoded: ByteArray): Any {
        return decode(encoded, 0)
    }

    /**
     * @return fixed size in bytes. For the dynamic types returns IntType.getFixedSize()
     * which is effectively the int offset to dynamic data
     */
    open fun getFixedSize(): Int {
        return 32
    }

    open fun isDynamicType(): Boolean {
        return false
    }

    override fun toString(): String {
        return name
    }
}

open class BytesType : SolidityType {
    protected constructor(name: String) : super(name)
    constructor() : super("bytes")

    override fun encode(value: Any): ByteArray {
        val bb = if (value is ByteArray) {
            value
        } else if (value is String) {
            value.toByteArray()
        } else {
            throw RuntimeException("byte[] or String value is expected for type 'bytes'")
        }
        val ret = ByteArray(((bb.size - 1) / 32 + 1) * 32) // padding 32 bytes
        System.arraycopy(bb, 0, ret, 0, bb.size)
        return IntType.encodeInt(bb.size) + ret
    }

    override fun decode(encoded: ByteArray, offset: Int): Any {
        var offset = offset
        val len: Int = IntType.decodeInt(encoded, offset).toInt()
        if (len == 0) return ByteArray(0)
        offset += 32
        return encoded.copyOfRange(offset, offset + len)
    }

    override fun isDynamicType(): Boolean {
        return true
    }
}

class StringType : BytesType("string") {
    override fun encode(value: Any): ByteArray {
        if (value !is String) throw java.lang.RuntimeException("String value expected for type 'string'")
        // return super.encode(((String) value).getSignBytes(StandardCharsets.UTF_8));
        return try {
            super.encode(value.toByteArray(charset("UTF-8")))
        } catch (e: UnsupportedEncodingException) {
            throw java.lang.RuntimeException(e)
        }
    }

    override fun decode(encoded: ByteArray, offset: Int): Any {
        return try {
            String((super.decode(encoded, offset) as ByteArray?)!!, Charset.forName("UTF-8"))
        } catch (e: UnsupportedEncodingException) {
            throw java.lang.RuntimeException(e)
        }
    }
}

open class Bytes32Type(s: String) : SolidityType(s) {
    override fun encode(value: Any): ByteArray {
        if (value is Number) {
            val bigInt = BigInteger(value.toString())
            return IntType.encodeInt(bigInt)
        } else if (value is String) {
            val ret = ByteArray(32)
            val bytes = try {
                value.toByteArray(charset("UTF-8"))
            } catch (e: UnsupportedEncodingException) {
                throw java.lang.RuntimeException(e)
            }
            System.arraycopy(bytes, 0, ret, 0, bytes.size)
            return ret
        } else if (value is ByteArray) {
            val ret = ByteArray(32)
            System.arraycopy(value, 0, ret, 0, value.size)
            return ret
        }
        throw RuntimeException("Can't encode java type " + value.javaClass + " to bytes32")
    }

    override fun decode(encoded: ByteArray, offset: Int): Any {
        return encoded.copyOfRange(offset, offset + getFixedSize())
    }
}

class AddressType : IntType("address") {
    override fun encode(value: Any): ByteArray {
        var value = value
        if (value is String && !value.startsWith("0x")) {
            // address is supposed to be always in hex
            value = "0x$value"
        }
        val addr = super.encode(value)
        for (i in 0..11) {
            if (addr[i] != 0.toByte()) {
                throw RuntimeException(
                    "Invalid address (should be 20 bytes length): " + addr.toHex()
                )
            }
        }
        return addr
    }

    override fun decode(encoded: ByteArray, offset: Int): Any {
        val bi: BigInteger = super.decode(encoded, offset) as BigInteger
        return bi.toBytes(20)
    }
}

open class IntType(name: String) : SolidityType(name) {
    override fun getCanonicalName(): String {
        if (name == "int") return "int256"
        return if (name == "uint") "uint256" else super.getCanonicalName()
    }

    override fun encode(value: Any): ByteArray {
        val bigInt: BigInteger
        if (value is String) {
            var s = value.toLowerCase().trim { it <= ' ' }
            var radix = 10
            if (s.startsWith("0x")) {
                s = s.substring(2)
                radix = 16
            } else if (s.contains("a") || s.contains("b") || s.contains("c") ||
                s.contains("d") || s.contains("e") || s.contains("f")
            ) {
                radix = 16
            }
            bigInt = BigInteger(s, radix)
        } else if (value is BigInteger) {
            bigInt = value
        } else if (value is Number) {
            bigInt = BigInteger(value.toString())
        } else if (value is ByteArray) {
            bigInt = value.toBigInteger()
        } else {
            throw java.lang.RuntimeException("Invalid value for type '" + this + "': " + value + " (" + value!!.javaClass + ")")
        }
        return encodeInt(bigInt)
    }

    override fun decode(encoded: ByteArray, offset: Int): Any {
        return decodeInt(encoded, offset)
    }

    companion object {
        fun decodeInt(encoded: ByteArray, offset: Int): BigInteger {
            return BigInteger(encoded.copyOfRange(offset, offset + 32))
        }

        fun encodeInt(i: Int): ByteArray {
            return encodeInt(BigInteger("" + i))
        }

        fun encodeInt(bigInt: BigInteger): ByteArray {
            return bigInt.toBytes(32)
        }
    }
}

class BoolType : IntType("bool") {
    override fun encode(value: Any): ByteArray {
        if (value !is Boolean) throw java.lang.RuntimeException("Wrong value for bool type: $value")
        return super.encode(if (value == true) 1 else 0)
    }

    override fun decode(encoded: ByteArray, offset: Int): Any {
        return java.lang.Boolean.valueOf((super.decode(encoded, offset) as Number?)!!.toInt() != 0)
    }
}

class FunctionType : Bytes32Type("function") {
    override fun encode(value: Any): ByteArray {
        if (value !is ByteArray) throw java.lang.RuntimeException("Expected byte[] value for FunctionType")
        if (value.size != 24) throw java.lang.RuntimeException("Expected byte[24] for FunctionType")
        return super.encode(value + ByteArray(8))
    }
}

abstract class ArrayType(name: String) : SolidityType(name) {
    var elementType: SolidityType

    override fun encode(value: Any): ByteArray {
        return if (value.javaClass.isArray) {
            val elems: MutableList<Any> = ArrayList()
            for (i in 0 until java.lang.reflect.Array.getLength(value)) {
                elems.add(
                    java.lang.reflect.Array.get(value, i)
                        ?: throw RuntimeException("List value expected for type $name")
                )
            }
            encodeList(elems)
        } else if (value is List<*>) {
            encodeList(value)
        } else {
            throw RuntimeException("List value expected for type $name")
        }
    }

    abstract fun encodeList(l: List<*>): ByteArray

    companion object {
        fun getType(typeName: String): ArrayType {
            val idx1 = typeName.indexOf("[")
            val idx2 = typeName.indexOf("]", idx1)
            return if (idx1 + 1 == idx2) {
                DynamicArrayType(typeName)
            } else {
                StaticArrayType(typeName)
            }
        }
    }

    init {
        val idx = name.indexOf("[")
        val st = name.substring(0, idx)
        val idx2 = name.indexOf("]", idx)
        val subDim = if (idx2 + 1 == name.length) "" else name.substring(idx2 + 1)
        elementType = SolidityType.getType(st + subDim)
    }
}

class StaticArrayType(name: String) : ArrayType(name) {
    var size: Int
    override fun getCanonicalName(): String {
        return elementType.getCanonicalName() + "[" + size + "]"
    }

    override fun encodeList(l: List<*>): ByteArray {
        if (l.size != size) throw java.lang.RuntimeException("List size (" + l.size + ") != " + size + " for type " + name)
        val elems = arrayOfNulls<ByteArray>(size)
        for (i in l.indices) {
            elems[i] = elementType.encode(l[i]!!)
        }
        return elems.merge()
    }

    override fun decode(encoded: ByteArray, offset: Int): Array<Any?> {
        val result = arrayOfNulls<Any>(size)
        for (i in 0 until size) {
            result[i] = elementType.decode(encoded, offset + i * elementType.getFixedSize())
        }
        return result
    }

    override fun getFixedSize(): Int {
        // return negative if elementType is dynamic
        return elementType.getFixedSize() * size
    }

    init {
        val idx1 = name.indexOf("[")
        val idx2 = name.indexOf("]", idx1)
        val dim = name.substring(idx1 + 1, idx2)
        size = dim.toInt()
    }
}

class DynamicArrayType(name: String) : ArrayType(name) {
    override fun getCanonicalName(): String {
        return elementType.getCanonicalName() + "[]"
    }

    override fun encodeList(l: List<*>): ByteArray {
        val elems: Array<ByteArray?>
        if (elementType.isDynamicType()) {
            elems = arrayOfNulls(l.size * 2 + 1)
            elems[0] = IntType.encodeInt(l.size)
            var offset = l.size * 32
            for (i in l.indices) {
                elems[i + 1] = IntType.encodeInt(offset)
                val encoded = elementType.encode(l[i]!!)
                elems[l.size + i + 1] = encoded
                offset += 32 * ((encoded.size - 1) / 32 + 1)
            }
        } else {
            elems = arrayOfNulls(l.size + 1)
            elems[0] = IntType.encodeInt(l.size)
            for (i in l.indices) {
                elems[i + 1] = elementType.encode(l[i]!!)
            }
        }
        return elems.merge()
    }

    override fun decode(encoded: ByteArray, origOffset: Int): Any {
        var origOffset = origOffset
        val len: Int = IntType.decodeInt(encoded, origOffset).toInt()
        origOffset += 32
        var offset = origOffset
        val ret = arrayOfNulls<Any>(len)
        for (i in 0 until len) {
            if (elementType.isDynamicType()) {
                ret[i] = elementType.decode(
                    encoded,
                    origOffset + IntType.decodeInt(encoded, offset).toInt()
                )
            } else {
                ret[i] = elementType.decode(encoded, offset)
            }
            offset += elementType.getFixedSize()
        }
        return ret
    }

    override fun isDynamicType(): Boolean {
        return true
    }
}