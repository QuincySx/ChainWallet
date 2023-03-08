package com.smallraw.chain.ethereum

import com.smallraw.chain.ethereum.execptions.EthereumException
import com.smallraw.crypto.core.extensions.hexToByteArray

class PublicKey(private val key: ByteArray) {
    companion object {
        @Throws(EthereumException.KeyWrongLengthException::class)
        fun ofHex(hexString: String): PublicKey {
            return PublicKey(hexString.hexToByteArray())
        }
    }

    private val _address by lazy {
        Address.ofPublicKey(this)
    }

    // 防止外部修改公钥
    fun getKey() = key.copyOf()

    fun getAddress() = _address
}