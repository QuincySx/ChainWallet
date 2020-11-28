package com.smallraw.wallet.hd

import java.math.BigInteger

class HDKey : ECKey {
    companion object {
        const val HARDENED_FLAG = 0x80000000
    }

    private val chainCode: ByteArray
    val depth: Int
    private val childNumber: Int
    private val isHardened: Boolean

    @Throws(IllegalArgumentException::class)
    constructor(
        privKey: BigInteger,
        chainCode: ByteArray,
        parent: HDKey? = null,
        childNumber: Int = 0,
        isHardened: Boolean = true
    ) : super(privKey) {
        val privateKey = privKey.toByteArray()
        if (privateKey.size > 33)
            throw  IllegalArgumentException("Private key is longer than 33 bytes")
        if (chainCode.size != 32)
            throw IllegalArgumentException("Chain code is not 32 bytes")
        this.chainCode = chainCode.copyOfRange(0, chainCode.size)
        this.depth = if (parent != null) parent.depth + 1 else 0
        this.childNumber = childNumber
        this.isHardened = isHardened
    }

    @Throws(IllegalArgumentException::class)
    constructor(
        publicKey: ByteArray,
        chainCode: ByteArray,
        parent: HDKey? = null,
        childNumber: Int = 0,
        isHardened: Boolean = true
    ) : super(publicKey) {
        if (publicKey.size != 33)
            throw  IllegalArgumentException("Private key is longer than 33 bytes")
        if (chainCode.size != 32)
            throw IllegalArgumentException("Chain code is not 32 bytes")
        this.chainCode = chainCode.copyOfRange(0, chainCode.size)
        this.depth = if (parent != null) parent.depth + 1 else 0
        this.childNumber = childNumber
        this.isHardened = isHardened
    }

    fun getChainCode() = chainCode

    fun getPaddedPrivKeyBytes(): ByteArray {
        if (privateKey == null) {
            return ByteArray(33)
        } else {
            val privKeyBytes: ByteArray = privateKey.toByteArray()
            val paddedBytes = ByteArray(33)
            System.arraycopy(
                privKeyBytes,
                0,
                paddedBytes,
                33 - privKeyBytes.size,
                privKeyBytes.size
            )
            return paddedBytes
        }
    }
}