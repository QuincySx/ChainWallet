package com.smallraw.chain.lib.bitcoin

import com.smallraw.chain.lib.*
import com.smallraw.chain.lib.crypto.Secp256k1Signer
import com.smallraw.chain.lib.bitcoin.convert.WalletImportFormat
import com.smallraw.chain.lib.bitcoin.network.Network
import com.smallraw.chain.lib.bitcoin.network.TestNet
import java.security.PrivateKey
import java.security.PublicKey

class BitcoinAccount(
    mPrivateKey: PrivateKey? = null,
    mPublicKey: PublicKey? = null,
    private val network: Network = TestNet(),
    private val compressed: Boolean = true
) {
    private val mKeyPair = Secp256k1KeyPair(mPrivateKey, mPublicKey)
    private val mSigner by lazy {
        Secp256k1Signer()
    }
    private val mBitcoinAddress by lazy {
        BitcoinP2PKHAddress.fromPublicKey(getPublicKey(), network)
    }
    private val mWalletImportFormat by lazy {
        WalletImportFormat(network, compressed)
    }

    fun getPublicKey() = mKeyPair.getPublicKey()

    fun getPrivateKey() = mKeyPair.getPrivateKey()

    fun getAddress() = mBitcoinAddress

    fun sign(message: ByteArray): Signature {
        return mSigner.sign(mKeyPair.getPrivateKey(), message)
    }

    fun getWifPrivateKey(): String {
        return mWalletImportFormat.format(getPrivateKey().encoded)
    }
}