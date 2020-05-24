package com.smallraw.chain.lib

import com.smallraw.chain.lib.crypto.BitcoinPublicGenerator
import com.smallraw.chain.lib.crypto.Secp256k1Signer
import com.smallraw.chain.lib.format.WalletImportFormat
import java.security.PrivateKey
import java.security.PublicKey

class BitcoinAccount(
    mPrivateKey: PrivateKey? = null,
    mPublicKey: PublicKey? = null,
    private val testNet: Boolean = true,
    private val compressed: Boolean = true
) : ChainAccount(
    mPrivateKey,
    mPublicKey
) {
    private val mWalletImportFormat by lazy {
        WalletImportFormat(testNet, compressed)
    }

    fun getWifPrivateKey(): String? {
        if (mPrivateKey == null) {
            return null
        }
        return mWalletImportFormat.format(mPrivateKey.encoded)
    }

    override fun createPublicGenerator(): PublicGenerator {
        return BitcoinPublicGenerator(compressed)
    }

    override fun createAddress(mPublicKey: PublicKey): Address {
        return BitcoinAddress(mPublicKey, testNet)
    }

    override fun createSigner(): Signer {
        return Secp256k1Signer()
    }
}