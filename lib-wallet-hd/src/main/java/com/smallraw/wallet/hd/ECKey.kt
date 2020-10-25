package com.smallraw.wallet.hd

import com.smallraw.lib.crypto.Secp256K1
import java.math.BigInteger
import java.util.*

open class ECKey {
    protected val privateKey: BigInteger?
    protected val publicKey: ByteArray
    private val isCompressed: Boolean

    constructor(privKey: BigInteger, compressed: Boolean = true) : this(
        null,
        privKey,
        compressed
    )

    constructor(pubKey: ByteArray) : this(pubKey, null, false)

    constructor(
        pubKey: ByteArray? = null,
        privKey: BigInteger? = null,
        compressed: Boolean = true
    ) {
        privateKey = privKey
        when {
            pubKey != null -> {
                this.publicKey = Arrays.copyOfRange(pubKey, 0, pubKey.size)
                isCompressed = (pubKey.size == 33);
            }
            privKey != null -> {
                this.publicKey = pubKeyFromPrivKey(privKey, compressed)
                isCompressed = compressed
            }
            else -> {
                throw IllegalArgumentException("You must provide at least a private key or a public key");
            }
        }
    }

    open fun getPubKey(): ByteArray {
        return publicKey
    }

    open fun isCompressed(): Boolean {
        return isCompressed
    }

    open fun getPrivKey(): BigInteger? {
        return privateKey
    }

    open fun getPrivKeyBytes(): ByteArray? {
        return privateKey?.toByteArray()
    }

    private fun pubKeyFromPrivKey(privKey: BigInteger, compressed: Boolean): ByteArray {
        return Secp256K1.createPublicKey(privKey.toByteArray(), compressed)
    }
}