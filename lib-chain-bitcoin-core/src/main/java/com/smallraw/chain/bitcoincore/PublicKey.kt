package com.smallraw.chain.bitcoincore

import com.smallraw.chain.bitcoincore.address.P2PKHAddress
import com.smallraw.chain.bitcoincore.address.P2WPKHAddress
import com.smallraw.chain.bitcoincore.execptions.BitcoinException
import com.smallraw.chain.bitcoincore.network.BaseNetwork
import com.smallraw.chain.lib.core.crypto.Ripemd160
import com.smallraw.chain.lib.core.extensions.hexToByteArray

class PublicKey(private val key: ByteArray) {
    companion object {
        @Throws(BitcoinException.KeyWrongLengthException::class)
        fun ofHex(hexString: String): PublicKey {
            return PublicKey(hexString.hexToByteArray())
        }
    }

    fun getHash() = Ripemd160.hash160(key)

    // 防止外部修改公钥
    fun getKey() = key.copyOf()

    fun getAddress(network: BaseNetwork, isSegwit: Boolean = false) = if (isSegwit) {
        P2WPKHAddress(getHash(), network.addressSegwitHrp, null)
    } else {
        P2PKHAddress(getHash(), network.addressVersion)
    }
}