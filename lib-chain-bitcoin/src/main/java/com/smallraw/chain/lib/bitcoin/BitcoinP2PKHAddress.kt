package com.smallraw.chain.lib.bitcoin

import com.smallraw.chain.lib.crypto.Base58
import com.smallraw.chain.lib.crypto.Ripemd160
import java.security.PublicKey

class BitcoinP2PKHAddress(
    hashKey: ByteArray,
    testNet: Boolean
) : BitcoinAddress(
    AddressType.P2PKH, hashKey, if (testNet) {
        TEST_NET_ADDRESS_SUFFIX
    } else {
        MAIN_NET_ADDRESS_SUFFIX
    }
) {
    companion object {
        private const val TEST_NET_ADDRESS_SUFFIX = 0x6f.toByte()
        private const val MAIN_NET_ADDRESS_SUFFIX = 0x00.toByte()

        fun fromPublicKey(publicKey: PublicKey, testNet: Boolean = false): BitcoinP2PKHAddress {
            return BitcoinP2PKHAddress(Ripemd160.hash160(publicKey.encoded), testNet)
        }

        fun fromAddress(address: String): BitcoinP2PKHAddress {
            val decodeCheck = Base58.decodeCheck(address)
            val isTestNet = when (decodeCheck[0]) {
                TEST_NET_ADDRESS_SUFFIX -> true
                MAIN_NET_ADDRESS_SUFFIX -> false
                else -> true
            }
            val hashKey = ByteArray(decodeCheck.size - 1)
            System.arraycopy(decodeCheck, 1, hashKey, 0, hashKey.size)
            return BitcoinP2PKHAddress(hashKey, isTestNet)
        }
    }
}