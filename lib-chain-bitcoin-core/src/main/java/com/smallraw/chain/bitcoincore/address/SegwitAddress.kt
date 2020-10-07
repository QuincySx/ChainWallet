package com.smallraw.chain.bitcoincore.address

import com.smallraw.chain.bitcoincore.crypto.Bech32Segwit
import com.smallraw.chain.bitcoincore.execptions.BitcoinFormatException
import com.smallraw.chain.bitcoincore.network.BaseNetwork
import com.smallraw.chain.bitcoincore.script.Chunk
import com.smallraw.chain.bitcoincore.script.ChunkData
import com.smallraw.chain.bitcoincore.script.OP_0
import com.smallraw.chain.bitcoincore.script.Script
import com.smallraw.chain.lib.crypto.Ripemd160
import com.smallraw.chain.lib.crypto.Sha256
import com.smallraw.chain.lib.extensions.plus
import java.lang.Exception

abstract class SegwitAddress(
    network: BaseNetwork,
    address: String? = null,
    witnessHash: ByteArray? = null,
    script: Script? = null,
    private val version: Int = 0,
) : Address(network) {
    private val hashKeyBytes: ByteArray

    init {
        hashKeyBytes = when {
            address != null -> {
                addressToHash(address)
            }
            witnessHash != null -> {
                witnessHash
            }
            script != null -> {
                scriptToHash(script)
            }
            else -> throw BitcoinFormatException.AddressInitException("A valid address or witnessHash or script is required.")
        }
    }

    private val address: String by lazy {
        val witnessScript = Bech32Segwit.convertBits(hashKeyBytes, 0, hashKeyBytes.size, 8, 5, true)
        Bech32Segwit.encode(network.addressSegwitHrp, version + witnessScript)
    }

    private fun scriptToHash(script: Script): ByteArray {
        return Sha256.sha256(script.scriptBytes)
    }

    private fun addressToHash(address: String): ByteArray {
        val decoded = Bech32Segwit.decode(address)

        if (decoded.hrp != network.addressSegwitHrp) {
            throw BitcoinFormatException.AddressFormatException("Address HRP ${decoded.hrp} is not correct")
        }

        if (decoded.data[0] != version.toByte()) {
            throw BitcoinFormatException.AddressFormatException("Address Segwit Version Error")
        }
        try {
            val payload = decoded.data
            return Bech32Segwit.convertBits(payload, 1, payload.size - 1, 5, 8, false)
        } catch (e: Exception) {
            throw BitcoinFormatException.AddressFormatException("segwit address error")
        }
    }

    override fun toHash() = hashKeyBytes

    override fun toString() = address
}

class P2WPKHAddress(
    network: BaseNetwork,
    address: String? = null,
    hashKey: ByteArray? = null,
    publicKey: ByteArray? = null
) : SegwitAddress(
    network,
    address,
    hashKey ?: publicKey?.let { Ripemd160.hash160(it) },
    null
) {
    override fun getType() = Address.AddressType.P2WPKHV0

    override fun lockScript(): Script {
        return Script(
            Chunk { OP_0 },
            ChunkData { toHash() }
        )
    }
}

class P2WSHAddress(
    network: BaseNetwork,
    address: String? = null,
    witnessHash: ByteArray? = null,
    script: Script? = null,
) : SegwitAddress(network, address, witnessHash, script) {
    override fun getType() = Address.AddressType.P2WSHV0

    override fun lockScript(): Script {
        return Script(
            Chunk { OP_0 },
            ChunkData { toHash() }
        )
    }
}