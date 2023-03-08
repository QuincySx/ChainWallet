package com.smallraw.chain.ethereum

import com.smallraw.chain.ethereum.extensions.toHexPrefix
import com.smallraw.chain.ethereum.supplement.EIP55
import com.smallraw.crypto.core.crypto.Keccak
import com.smallraw.crypto.core.extensions.hexToByteArray

class Address(val raw: ByteArray) {
    companion object {
        fun ofPublicKey(publicKey: PublicKey): Address {
            return Address(Keccak.sha256(publicKey.getKey()).takeLast(20).toByteArray())
        }
    }

    init {
        AddressValidator.validate(hex)
    }

    constructor(hex: String) : this(hex.hexToByteArray())

    val hex: String
        get() = raw.toHexPrefix()

    val eip55: String
        get() = EIP55.format(hex)

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true

        return if (other is Address)
            raw.contentEquals(other.raw)
        else false
    }

    override fun hashCode(): Int {
        return raw.contentHashCode()
    }

    override fun toString(): String {
        return hex
    }

}