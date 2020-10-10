package com.smallraw.chain.bitcoincore.address

import com.smallraw.chain.bitcoincore.execptions.BitcoinException
import com.smallraw.chain.bitcoincore.script.Chunk
import com.smallraw.chain.bitcoincore.script.OP_CHECKSIG
import com.smallraw.chain.bitcoincore.script.OP_DUP
import com.smallraw.chain.bitcoincore.script.OP_EQUAL
import com.smallraw.chain.bitcoincore.script.OP_EQUALVERIFY
import com.smallraw.chain.bitcoincore.script.OP_HASH160
import com.smallraw.chain.bitcoincore.script.Script
import com.smallraw.chain.lib.core.crypto.Base58
import com.smallraw.chain.lib.core.extensions.plus

abstract class LegacyAddress(
    private val hashKeyBytes: ByteArray,
    private val addressVersion: Int? = null,
    protected var address: String? = null
) : Address {

    override fun toHash() = hashKeyBytes.copyOf()

    override fun toString(): String {
        return address ?: synchronized(this) {
            if (addressVersion == null) {
                throw BitcoinException.AddressFormatException("AddressVersion and Address must be one.")
            }
            address ?: Base58.encodeCheck(addressVersion + hashKeyBytes).also {
                address = it
            }
        }
    }
}

class P2PKHAddress : LegacyAddress {
    constructor(hashKeyBytes: ByteArray, address: String) : super(hashKeyBytes, null, address)
    constructor(hashKeyBytes: ByteArray, addressVersion: Int, address: String? = null) : super(
        hashKeyBytes,
        addressVersion,
        address
    )

    override fun getType() = Address.AddressType.P2PKH

    override fun scriptPubKey(): Script {
        return Script(
            Chunk(OP_DUP),
            Chunk(OP_HASH160),
            Chunk(toHash()),
            Chunk(OP_EQUALVERIFY),
            Chunk(OP_CHECKSIG)
        )
    }
}

class P2SHAddress : LegacyAddress {
    constructor(hashKeyBytes: ByteArray, address: String) : super(hashKeyBytes, null, address)
    constructor(hashKeyBytes: ByteArray, addressVersion: Int, address: String? = null) : super(
        hashKeyBytes,
        addressVersion,
        address
    )

    override fun getType() = Address.AddressType.P2SH

    override fun scriptPubKey(): Script {
        return Script(
            Chunk(OP_HASH160),
            Chunk(toHash()),
            Chunk(OP_EQUAL)
        )
    }
}