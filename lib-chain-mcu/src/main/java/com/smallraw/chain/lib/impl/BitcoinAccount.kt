package com.smallraw.chain.lib.impl

import com.smallraw.chain.lib.ChainAccount
import com.smallraw.chain.lib.crypto.impl.BitcoinPublicGenerator
import com.smallraw.chain.lib.crypto.impl.Secp256k1Signer
import java.security.PrivateKey
import java.security.PublicKey

class BitcoinAccount(
    mPrivateKey: PrivateKey? = null,
    mPublicKey: PublicKey? = null,
    private val testNet: Boolean = true,
    private val compressed: Boolean = true
) : ChainAccount(
    BitcoinPublicGenerator(),
    BitcoinAddress(),
    Secp256k1Signer(),
    mPrivateKey,
    mPublicKey
)