package com.smallraw.chain.ethereum.supplement

import com.smallraw.chain.ethereum.extensions.compleHexPrefix
import com.smallraw.chain.ethereum.extensions.stripHexPrefix
import com.smallraw.crypto.core.crypto.Keccak
import com.smallraw.crypto.core.extensions.toHex
import java.util.*

/**
 * 地址校验格式
 * see http://eips.ethereum.org/EIPS/eip-55
 */
object EIP55 {
    fun encode(data: ByteArray): String {
        return format(data.toHex().compleHexPrefix())
    }

    fun format(address: String): String {
        val lowercaseAddress = address.stripHexPrefix().lowercase(Locale.ENGLISH)
        val addressHash = Keccak.sha256(lowercaseAddress.toByteArray()).toHex()

        val result = StringBuilder(lowercaseAddress.length + 2)

        result.append("0x")

        for (i in lowercaseAddress.indices) {
            if (Integer.parseInt(addressHash[i].toString(), 16) >= 8) {
                result.append(lowercaseAddress[i].toString().uppercase(Locale.ENGLISH))
            } else {
                result.append(lowercaseAddress[i])
            }
        }

        return result.toString()
    }

    fun verify(data: ByteArray): Boolean {
        return verify(data.toHex().compleHexPrefix())
    }

    fun verify(address: String): Boolean {
        return address.equals(format(address))
    }
}