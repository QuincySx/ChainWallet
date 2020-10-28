package com.smallraw.wallet.hd

import com.smallraw.crypto.core.crypto.HmacSha2
import com.smallraw.crypto.core.extensions.hexToByteArray
import com.smallraw.lib.crypto.Secp256K1
import java.math.BigInteger
import java.nio.ByteBuffer

object HDKeyDerivation {
    private val secp256k1_N = BigInteger(
        1,
        "00fffffffffffffffffffffffffffffffebaaedce6af48a03bbfd25e8cd0364141".hexToByteArray()
    )

    @Throws(HDDerivationException::class)
    fun createRootKey(seed: ByteArray): HDKey {
        if (seed.size < 16)
            throw IllegalArgumentException("Seed must be at least 128 bits")

        val i = HmacSha2.sha512("Bitcoin seed".toByteArray(), seed)
        val il: ByteArray = i.copyOfRange(0, 32)
        val ir: ByteArray = i.copyOfRange(32, 64)
        val privKey = BigInteger(1, il)
        if (privKey.signum() == 0)
            throw HDDerivationException("Generated master private key is zero")
        if (privKey >= secp256k1_N)
            throw HDDerivationException("Generated master private key is not less than N")
        return HDKey(privKey, ir, null, 0, false)
    }

    @Throws(HDDerivationException::class)
    fun deriveChildKey(parent: HDKey, childNumber: Int, hardened: Boolean): HDKey {
        if ((childNumber and HDKey.HARDENED_FLAG.toInt()) != 0)
            throw IllegalArgumentException("Hardened flag must not be set in child number")

        return if (parent.getPrivKeyBytes() == null) {
            if (hardened)
                throw IllegalStateException("Hardened key requires parent private key");
            derivePublicKey(parent, childNumber)
        } else {
            derivePrivateKey(parent, childNumber, hardened);
        }
    }

    private fun derivePrivateKey(parent: HDKey, childNumber: Int, hardened: Boolean): HDKey {
        val parentPubKey = parent.getPubKey()
        if (parentPubKey?.size != 33) {
            throw IllegalStateException("Parent public key is not 33 bytes")
        }

        val dataBuffer: ByteBuffer = ByteBuffer.allocate(37)
        if (hardened) {
            dataBuffer.put(parent.getPaddedPrivKeyBytes())
                .putInt(childNumber or HDKey.HARDENED_FLAG.toInt())
        } else {
            dataBuffer.put(parentPubKey)
                .putInt(childNumber)
        }
        //028185cc658b46dd444a78bd8788e1b0e2f45ab4bc26942f600384dc73f7560a4800000000
        val i: ByteArray = HmacSha2.sha512(parent.getChainCode(), dataBuffer.array())
        val il: ByteArray = i.copyOfRange(0, 32)
        val ir: ByteArray = i.copyOfRange(32, 64)
        val ilInt = BigInteger(1, il)
        if (ilInt.signum() == 0)
            throw HDDerivationException("Generated master private key is zero")
        if (ilInt >= secp256k1_N)
            throw HDDerivationException("Generated master private key is not less than N")
        val privKeyInt: BigInteger =
            parent.getPrivKey()?.add(ilInt)?.mod(secp256k1_N) ?: BigInteger("0")
        if (privKeyInt.signum() == 0)
            throw HDDerivationException("Derived private key is zero");
        return HDKey(privKeyInt, ir, parent, childNumber, hardened)
    }

    private fun derivePublicKey(parent: HDKey, childNumber: Int): HDKey {
        val dataBuffer = ByteBuffer.allocate(37)
        dataBuffer.put(parent.getPubKey()).putInt(childNumber)
        val i: ByteArray = HmacSha2.sha512(parent.getChainCode(), dataBuffer.array())
        val il: ByteArray = i.copyOfRange(0, 32)
        val ir: ByteArray = i.copyOfRange(32, 64)
        val publicKey = Secp256K1.createPublicKey(il, true)
        return HDKey(publicKey, ir, parent, childNumber, false)
    }
}