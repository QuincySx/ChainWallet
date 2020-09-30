package com.smallraw.chain.lib.provider

import com.smallraw.chain.lib.Secp256k1KeyPair
import com.smallraw.chain.lib.Secp256k1PrivateKey
import com.smallraw.chain.lib.bitcoin.convert.WalletImportFormat
import com.smallraw.chain.lib.bitcoin.transaction.build.IPrivateKeyPairProvider
import com.smallraw.chain.lib.bitcoin.Bitcoin
import com.smallraw.chain.lib.extensions.hexToByteArray
import java.security.PublicKey

/**
 * 测试私钥提供者
 */
class UnitTestIPrivateKeyPairProvider(private val wifPrivate: String) :
    IPrivateKeyPairProvider {
    override fun findByPrivate(publicKey: PublicKey): Bitcoin.KeyPair {
        return Bitcoin.KeyPair(
            Bitcoin.PrivateKey(wifPrivate.hexToByteArray()),
            null,
            true
        )
    }

    override fun findByAddress(address: Bitcoin.Address): Bitcoin.KeyPair {
        val decode =
            WalletImportFormat.decode(wifPrivate)
        return Bitcoin.KeyPair(Bitcoin.PrivateKey(decode.privateKey), null, true)
    }
}