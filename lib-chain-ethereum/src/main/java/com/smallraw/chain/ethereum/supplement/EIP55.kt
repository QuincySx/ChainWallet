package com.smallraw.chain.ethereum.supplement

import com.smallraw.chain.ethereum.extensions.compleHexPrefix
import com.smallraw.chain.ethereum.extensions.stripHexPrefix
import com.smallraw.crypto.core.crypto.Keccak
import com.smallraw.crypto.core.extensions.toHex
import java.util.*

object EIP55 {
    fun encode(data: ByteArray): String {
        return format(data.toHex().compleHexPrefix())
    }

    fun format(address: String): String {
        val lowercaseAddress = address.stripHexPrefix().toLowerCase(Locale.ENGLISH)
        val addressHash = Keccak.sha256(lowercaseAddress.toByteArray()).toHex()

        val result = StringBuilder(lowercaseAddress.length + 2)

        result.append("0x")

        for (i in lowercaseAddress.indices) {
            if (Integer.parseInt(addressHash[i].toString(), 16) >= 8) {
                result.append(lowercaseAddress[i].toString().toUpperCase(Locale.ENGLISH))
            } else {
                result.append(lowercaseAddress[i])
            }
        }

        return result.toString()
    }
}