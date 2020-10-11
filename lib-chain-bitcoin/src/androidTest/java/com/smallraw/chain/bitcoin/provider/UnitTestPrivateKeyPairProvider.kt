package com.smallraw.chain.bitcoin.provider

import com.smallraw.chain.bitcoin.Bitcoin
import com.smallraw.chain.bitcoin.convert.WalletImportFormat
import com.smallraw.chain.bitcoin.transaction.build.`interface`.IPrivateKeyPairProvider
import com.smallraw.chain.bitcoincore.PrivateKey
import com.smallraw.chain.bitcoincore.PublicKey
import com.smallraw.chain.bitcoincore.address.Address
import com.smallraw.chain.lib.core.extensions.hexToByteArray

/**
 * 测试私钥提供者
 */
class UnitTestPrivateKeyPairProvider(private val wifPrivate: String) :
    IPrivateKeyPairProvider {
    override fun findByPrivate(publicKey: PublicKey): Bitcoin.KeyPair {
        return Bitcoin.KeyPair(
            PrivateKey(wifPrivate.hexToByteArray()),
            null,
            true
        )
    }

    override fun findByAddress(address: Address): Bitcoin.KeyPair {
        val decode =
            WalletImportFormat.decode(wifPrivate)
        return Bitcoin.KeyPair(PrivateKey(decode.privateKey), null, true)
    }
}