package com.smallraw.chain.ethereum.rpl

import com.smallraw.crypto.core.extensions.toHex
import java.io.Serializable

class DecodeResult(val pos: Int, val decoded: Any) : Serializable {

    override fun toString(): String {
        return asString(this.decoded)
    }

    private fun asString(decoded: Any?): String = when (decoded) {
        is String -> decoded
        is ByteArray -> decoded.toHex()
        is Array<*> -> {
            val result = StringBuilder()
            for (item in decoded) {
                result.append(asString(item))
            }
            result.toString()
        }
        else -> ""
    }
}