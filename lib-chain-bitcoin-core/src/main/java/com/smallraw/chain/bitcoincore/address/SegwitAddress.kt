package com.smallraw.chain.bitcoincore.address

import com.smallraw.chain.bitcoincore.crypto.Bech32Segwit
import com.smallraw.chain.bitcoincore.execptions.BitcoinException
import com.smallraw.chain.bitcoincore.script.Chunk
import com.smallraw.chain.bitcoincore.script.OP_0
import com.smallraw.chain.bitcoincore.script.Script
import com.smallraw.crypto.core.extensions.plus

abstract class SegwitAddress(
    private val hashKeyBytes: ByteArray,
    private val addressSegwitHrp: String? = null,
    private var address: String? = null,
    private val version: Byte = OP_0,
) : Address {

    override fun toHash() = hashKeyBytes.copyOf()

    private fun calculateAddress(): String {
        if (addressSegwitHrp == null) {
            throw BitcoinException.AddressFormatException("AddressSegwitHrp and Address must be one.")
        }
        val witnessScript = Bech32Segwit.convertBits(hashKeyBytes, 0, hashKeyBytes.size, 8, 5, true)

        return Bech32Segwit.encode(addressSegwitHrp, version + witnessScript)
    }

    override fun toString(): String {
        return address ?: synchronized(this) {
            address ?: calculateAddress().also {
                address = it
            }
        }
    }

    override fun scriptPubKey(): Script {
        return Script(
            Chunk(version),
            Chunk(toHash())
        )
    }
}

class P2WPKHAddress : SegwitAddress {
    @Deprecated("不推荐使用，容易与另一个构造混淆。如果使用，请注意参数顺序。")
    constructor(hashKeyBytes: ByteArray, address: String, version: Byte = OP_0) : super(
        hashKeyBytes,
        null,
        address,
        version
    )

    constructor(
        hashKeyBytes: ByteArray,
        addressSegwitHrp: String,
        address: String? = null,
        version: Byte = OP_0
    ) : super(
        hashKeyBytes,
        addressSegwitHrp,
        address,
        version
    )

    override fun getType() = Address.AddressType.P2WPKHV0
}

class P2WSHAddress : SegwitAddress {
    @Deprecated("不推荐使用，容易与另一个构造混淆。如果使用，请注意参数顺序。")
    constructor(hashKeyBytes: ByteArray, address: String, version: Byte = OP_0) : super(
        hashKeyBytes,
        null,
        address,
        version
    )

    constructor(
        hashKeyBytes: ByteArray,
        addressSegwitHrp: String,
        address: String? = null,
        version: Byte = OP_0
    ) : super(
        hashKeyBytes,
        addressSegwitHrp,
        address,
        version
    )

    override fun getType() = Address.AddressType.P2WSHV0
}