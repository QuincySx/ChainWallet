package com.smallraw.chain.bitcoincore.address

import com.smallraw.chain.bitcoincore.execptions.BitcoinFormatException
import com.smallraw.chain.bitcoincore.network.BaseNetwork
import com.smallraw.chain.bitcoincore.script.*
import com.smallraw.chain.lib.crypto.Base58
import com.smallraw.chain.lib.crypto.Ripemd160
import com.smallraw.chain.lib.extensions.plus
import java.lang.Exception
import java.math.BigInteger

abstract class LegacyAddress(
    network: BaseNetwork,
    address: String? = null,
    hash160: ByteArray? = null,
    script: Script? = null,
) : Address(network) {
    private lateinit var hashKeyBytes: ByteArray

    private val address: String by lazy {
        val data = when (getType()) {
            AddressType.P2PKH -> {
                network.addressVersion + hashKeyBytes
            }
            AddressType.P2SH -> {
                network.addressScriptVersion + hashKeyBytes
            }
            else -> {
                hashKeyBytes
            }
        }
        Base58.encodeCheck(data)
    }

    init {
        hashKeyBytes = when {
            address != null -> {
                isAddressValid(address)
                addressToHash160(address)
            }
            hash160 != null -> {
                isHash160Valid(hash160)
                hash160
            }
            script != null -> {
                scriptToHash160(script)
            }
            else -> throw BitcoinFormatException.AddressInitException("A valid address or hash160 or script is required.")
        }
    }

    private fun addressToHash160(address: String): ByteArray {
        val decodeCheck = Base58.decodeCheck(address)
        return decodeCheck.copyOfRange(1, decodeCheck.size)
    }

    private fun scriptToHash160(script: Script): ByteArray {
        return Ripemd160.hash160(script.scriptBytes)
    }

    fun isHash160Valid(hash160: ByteArray): Boolean {
        try {
            check(hash160.size == 40) { return false }
            BigInteger(16, hash160)
        } catch (e: Exception) {
            return false
        }
        return true
    }

    fun isAddressValid(address: String): Boolean {
        if (address.length < 26 || address.length < 35) {
            return false
        }
        try {
            val decodeCheck = Base58.decodeCheck(address)
            val network = when (getType()) {
                AddressType.P2PKH -> network.addressVersion.toByte()
                AddressType.P2SH -> network.addressScriptVersion.toByte()
                else -> null
            }

            if (network != null && decodeCheck[0] != network) {
                return false
            }
        } catch (e: Exception) {
            return false
        }
        return true
    }

    override fun toHash() = hashKeyBytes

    override fun toString() = address
}

class P2PKHAddress(
    network: BaseNetwork,
    address: String? = null,
    hashKey: ByteArray? = null,
    publicKey: ByteArray? = null
) : LegacyAddress(
    network,
    address,
    hashKey ?: publicKey?.let { Ripemd160.hash160(it) },
    null
) {

    override fun getType() = Address.AddressType.P2PKH

    override fun lockScript(): Script {
        return Script(
            Chunk { OP_DUP },
            Chunk { OP_HASH160 },
            ChunkData { toHash() },
            Chunk { OP_EQUALVERIFY },
            Chunk { OP_CHECKSIG }
        )
    }
}

class P2SHAddress(
    network: BaseNetwork,
    address: String? = null,
    hash160: ByteArray? = null,
    script: Script? = null,
) : LegacyAddress(network, address, hash160, script) {
    override fun getType() = AddressType.P2SH

    override fun lockScript(): Script {
        return Script(
            Chunk { OP_HASH160 },
            ChunkData { toHash() },
            Chunk { OP_EQUAL }
        )
    }
}