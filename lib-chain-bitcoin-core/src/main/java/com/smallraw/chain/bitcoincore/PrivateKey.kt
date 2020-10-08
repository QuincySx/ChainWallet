package com.smallraw.chain.bitcoincore

import com.smallraw.chain.bitcoincore.crypto.Secp256k1Signer
import com.smallraw.chain.bitcoincore.execptions.BitcoinException
import com.smallraw.chain.bitcoincore.script.SigHash
import com.smallraw.chain.lib.Secp256k1KeyPair
import com.smallraw.chain.lib.Secp256k1PrivateKey
import com.smallraw.chain.lib.core.crypto.DEREncode
import com.smallraw.chain.lib.core.execptions.JNICallException
import com.smallraw.chain.lib.core.extensions.hexToByteArray
import java.math.BigInteger
import kotlin.math.min
import kotlin.random.Random

class PrivateKey(private val secret: ByteArray) {
    companion object {
        @Throws(BitcoinException.KeyWrongLengthException::class)
        fun ofNumber(num: Long): PrivateKey {
            return ofNumber(num.toBigInteger())
        }

        @Throws(BitcoinException.KeyWrongLengthException::class)
        fun ofNumber(num: BigInteger): PrivateKey {
            val priv = num.toByteArray()
            if (priv.size > 32) {
                throw BitcoinException.KeyWrongLengthException("Private Key Wrong Length Exception: is 32 byte")
            }
            val byteArray = ByteArray(32)
            System.arraycopy(priv, 0, byteArray, 32 - priv.size, min(priv.size, 32))
            return PrivateKey(byteArray)
        }

        @Throws(BitcoinException.KeyWrongLengthException::class)
        fun ofHex(hexString: String): PrivateKey {
            return PrivateKey(hexString.hexToByteArray())
        }

        @Throws(BitcoinException.KeyWrongLengthException::class)
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
            throw BitcoinException.KeyWrongLengthException("Private Key Wrong Length Exception: is 32 byte")
        }
    }

    // 防止外部修改私钥
    fun getKey(): ByteArray = secret.copyOf()

    @Throws(BitcoinException.CalculatePublicKeyException::class)
    fun getPublicKey(compress: Boolean = true): PublicKey {
        try {
            return if (compress) {
                publicKeyCompress
            } else {
                publicKey
            }
        } catch (e: JNICallException) {
            throw BitcoinException.CalculatePublicKeyException()
        }
    }

    @Throws(BitcoinException.CalculateSignatureException::class)
    fun sign(message: ByteArray, sigHashValue: Byte = SigHash.ALL): Signature {
        try {
            val secpSignature = Secp256k1Signer().sign(Secp256k1PrivateKey(getKey()), message)
            val signature = DEREncode.sigToDer(secpSignature.signature()) + sigHashValue
            return Signature(signature)
        } catch (e: JNICallException) {
            throw BitcoinException.CalculateSignatureException()
        }
    }
}