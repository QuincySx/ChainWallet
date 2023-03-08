package com.smallraw.chain.eos

import com.smallraw.chain.eos.execptions.EOSException
import com.smallraw.crypto.core.crypto.Base58
import com.smallraw.crypto.core.extensions.hexToByteArray

class PublicKey(private val key: ByteArray) {
    companion object {
        const val address_prefix = "EOS"

        @Throws(EOSException.KeyWrongLengthException::class)
        fun ofHex(hexString: String): PublicKey {
            return PublicKey(hexString.hexToByteArray())
        }
    }

    // 防止外部修改公钥
    fun getKey() = key.copyOf()

    override fun toString(): String {
        val bf: StringBuffer = StringBuffer(address_prefix)
        bf.append(Base58.encode(key))
        return bf.toString()
    }
}