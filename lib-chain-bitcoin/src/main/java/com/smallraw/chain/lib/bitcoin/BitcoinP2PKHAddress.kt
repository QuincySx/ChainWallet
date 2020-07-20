package com.smallraw.chain.lib.bitcoin

import com.smallraw.chain.lib.bitcoin.network.Network
import com.smallraw.chain.lib.crypto.Base58
import com.smallraw.chain.lib.crypto.Ripemd160
import java.security.PublicKey

class BitcoinP2PKHAddress(
    hashKey: ByteArray,
    addressVersion: Int
) : BitcoinAddress(
    "", hashKey, AddressType.P2PKH
) {
    companion object {
        fun fromPublicKey(publicKey: PublicKey, netWork: Network): BitcoinP2PKHAddress {
            return BitcoinP2PKHAddress(Ripemd160.hash160(publicKey.encoded), netWork.addressVersion)
        }

        fun fromAddress(address: String): BitcoinP2PKHAddress {
            val decodeCheck = Base58.decodeCheck(address)
            val hashKey = ByteArray(decodeCheck.size - 1)
            System.arraycopy(decodeCheck, 1, hashKey, 0, hashKey.size)
            return BitcoinP2PKHAddress(hashKey, decodeCheck[0].toInt())
        }
    }
}