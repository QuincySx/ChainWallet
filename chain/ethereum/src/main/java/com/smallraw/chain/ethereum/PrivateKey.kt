package com.smallraw.chain.ethereum

import com.smallraw.chain.ethereum.crypto.Secp256k1Signer
import com.smallraw.chain.ethereum.execptions.EthereumException
import com.smallraw.crypto.Secp256k1KeyPair
import com.smallraw.crypto.Secp256k1PrivateKey
import com.smallraw.crypto.core.execptions.JNICallException
import com.smallraw.crypto.core.extensions.hexToByteArray
import java.math.BigInteger
import kotlin.math.min
import kotlin.random.Random

class PrivateKey(private val secret: ByteArray) {
    companion object {
        @Throws(EthereumException.KeyWrongLengthException::class)
        fun ofNumber(num: Long): PrivateKey {
            return ofNumber(num.toBigInteger())
        }

        @Throws(EthereumException.KeyWrongLengthException::class)
        fun ofNumber(num: BigInteger): PrivateKey {
            val priv = num.toByteArray()
            if (priv.size > 32) {
                throw EthereumException.KeyWrongLengthException("Private Key Wrong Length Exception: is 32 byte")
            }
            val byteArray = ByteArray(32)
            System.arraycopy(priv, 0, byteArray, 32 - priv.size, min(priv.size, 32))
            return PrivateKey(byteArray)
        }

        @Throws(EthereumException.KeyWrongLengthException::class)
        fun ofHex(hexString: String): PrivateKey {
            return PrivateKey(hexString.hexToByteArray())
        }

        @Throws(EthereumException.KeyWrongLengthException::class)
        fun new(): PrivateKey {
            return PrivateKey(Random.Default.nextBytes(32))
        }
    }

    private val publicKeyCompress: PublicKey by lazy {
        val encoded = Secp256k1KeyPair(
            Secp256k1PrivateKey(secret),
            compressed = false
        ).getPublicKey().encoded
        PublicKey(
            // 删除 04 开头
            encoded.copyOfRange(1, encoded.size)
        )
    }

    init {
        if (secret.size != 32) {
            throw EthereumException.KeyWrongLengthException("Private Key Wrong Length Exception: is 32 byte")
        }
    }

    // 防止外部修改私钥
    fun getKey(): ByteArray = secret.copyOf()

    @Throws(EthereumException.CalculatePublicKeyException::class)
    fun getPublicKey(): PublicKey {
        try {
            return publicKeyCompress
        } catch (e: JNICallException) {
            throw EthereumException.CalculatePublicKeyException()
        }
    }

    @Throws(EthereumException.CalculateSignatureException::class)
    fun sign(message: ByteArray): Signature {
        try {
            val secpSignature = Secp256k1Signer().sign(Secp256k1PrivateKey(getKey()), message)
            return secpSignature
        } catch (e: JNICallException) {
            throw EthereumException.CalculateSignatureException()
        }
    }
}