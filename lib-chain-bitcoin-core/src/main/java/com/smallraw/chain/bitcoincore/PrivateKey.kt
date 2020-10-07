package com.smallraw.chain.bitcoincore

import com.smallraw.chain.bitcoincore.crypto.Secp256k1Signer
import com.smallraw.chain.bitcoincore.execptions.BitcoinFormatException
import com.smallraw.chain.bitcoincore.script.SigHash
import com.smallraw.chain.lib.Secp256k1KeyPair
import com.smallraw.chain.lib.Secp256k1PrivateKey
import com.smallraw.chain.lib.crypto.DEREncode
import com.smallraw.chain.lib.extensions.hexToByteArray
import java.math.BigInteger
import kotlin.jvm.Throws
import kotlin.math.min
import kotlin.random.Random

class PrivateKey(private val secret: ByteArray) {
    companion object {
        @Throws(BitcoinFormatException.PrivateKeyWrongLengthException::class)
        fun ofNumber(num: Long): PrivateKey {
            return ofNumber(num.toBigInteger())
        }

        @Throws(BitcoinFormatException.PrivateKeyWrongLengthException::class)
        fun ofNumber(num: BigInteger): PrivateKey {
            val priv = num.toByteArray()
            if (priv.size > 32) {
                throw BitcoinFormatException.PrivateKeyWrongLengthException()
            }
            val byteArray = ByteArray(32)
            System.arraycopy(priv, 0, byteArray, 32 - priv.size, min(priv.size, 32))
            return PrivateKey(byteArray)
        }

        @Throws(BitcoinFormatException.PrivateKeyWrongLengthException::class)
        fun ofHex(hexString: String): PrivateKey {
            return PrivateKey(hexString.hexToByteArray())
        }

        @Throws(BitcoinFormatException.PrivateKeyWrongLengthException::class)
        fun new(): PrivateKey {
            return PrivateKey(Random.Default.nextBytes(32))
        }
    }

    private val publicKeyCompress: PublicKey by lazy {
        PublicKey(
            Secp256k1KeyPair(
                Secp256k1PrivateKey(secret),
                compressed = true
            ).getPublicKey().encoded
        )
    }

    private val publicKey: PublicKey by lazy {
        PublicKey(
            Secp256k1KeyPair(
                Secp256k1PrivateKey(secret),
                compressed = false
            ).getPublicKey().encoded
        )
    }

    init {
        if (secret.size != 32) {
            throw BitcoinFormatException.PrivateKeyWrongLengthException()
        }
    }

    fun getKey(): ByteArray = secret

    fun getPublicKey(compress: Boolean = true): PublicKey {
        return if (compress) {
            publicKeyCompress
        } else {
            publicKey
        }
    }

    fun sign(message: ByteArray, sigHashValue: Byte = SigHash.ALL): Signature {
        val secpSignature = Secp256k1Signer().sign(Secp256k1PrivateKey(getKey()), message)
        val signature = DEREncode.sigToDer(secpSignature.signature()) + sigHashValue
        return Signature(signature)
    }
}