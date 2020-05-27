package com.smallraw.chain.lib

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
    Secp256k1KeyPair(mPrivateKey, mPublicKey)
) {
    private val mWalletImportFormat by lazy {
        WalletImportFormat(testNet, compressed)
    }

    fun getWifPrivateKey(): String? {
        return mWalletImportFormat.format(getPrivateKey().encoded)
    }

    override fun createAddress(mPublicKey: PublicKey): Address {
        return BitcoinAddress(mPublicKey, testNet)
    }

    override fun createSigner(): Signer {
        return Secp256k1Signer()
    }
}