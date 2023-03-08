package com.smallraw.chain.ethereum.supplement

import com.smallraw.chain.ethereum.extensions.compleHexPrefix
import com.smallraw.chain.ethereum.extensions.stripHexPrefix
import com.smallraw.chain.ethereum.network.BaseNetwork
import com.smallraw.crypto.core.crypto.Keccak
import com.smallraw.crypto.core.extensions.toByteArray
import com.smallraw.crypto.core.extensions.toHex
import java.util.*

/**
 * 包含 chainID 的地址校验格式
 * see http://eips.ethereum.org/EIPS/eip-1191
 */
object ERC1191 {
    fun encode(data: ByteArray, network: BaseNetwork): String {
        return format(data.toHex().compleHexPrefix(), network)
    }

    fun format(address: String, network: BaseNetwork): String {
        val lowercaseAddress = address.stripHexPrefix().lowercase(Locale.ENGLISH)
        val addressHash =
            Keccak.sha256(network.id.toByteArray() + lowercaseAddress.toByteArray()).toHex()

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

    fun verify(data: ByteArray, network: BaseNetwork): Boolean {
        return verify(data.toHex().compleHexPrefix(), network)
    }

    fun verify(address: String, network: BaseNetwork): Boolean {
        return address.equals(format(address, network))
    }
}