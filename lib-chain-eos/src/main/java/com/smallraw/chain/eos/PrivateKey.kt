package com.smallraw.chain.eos

import com.smallraw.chain.eos.crypto.Secp256k1Signer
import com.smallraw.chain.eos.execptions.EOSException
import com.smallraw.crypto.Secp256k1KeyPair
import com.smallraw.crypto.Secp256k1PrivateKey
import com.smallraw.crypto.core.crypto.Base58
import com.smallraw.crypto.core.crypto.Ripemd160
import com.smallraw.crypto.core.crypto.Sha256
import com.smallraw.crypto.core.execptions.JNICallException
import com.smallraw.crypto.core.extensions.hexToByteArray
import java.math.BigInteger
import kotlin.math.min
import kotlin.random.Random

class PrivateKey(secret: ByteArray) {
    companion object {
        @Throws(EOSException.KeyWrongLengthException::class)
        fun ofNumber(num: Long): PrivateKey {
            return ofNumber(num.toBigInteger())
        }

        @Throws(EOSException.KeyWrongLengthException::class)
        fun ofNumber(num: BigInteger): PrivateKey {
            val priv = num.toByteArray()
            if (priv.size > 32) {
                throw EOSException.KeyWrongLengthException("Private Key Wrong Length Exception: is 32 byte")
            }
            val byteArray = ByteArray(32)
            System.arraycopy(priv, 0, byteArray, 32 - priv.size, min(priv.size, 32))
            return PrivateKey(byteArray)
        }

        @Throws(EOSException.KeyWrongLengthException::class)
        fun ofHex(hexString: String): PrivateKey {
            return PrivateKey(hexString.hexToByteArray())
        }

        @Throws(EOSException.KeyWrongLengthException::class)
        fun new(): PrivateKey {
            return PrivateKey(Random.Default.nextBytes(32))
        }

        const val PRIVATE_KEY_PREFIX = 0x80.toByte()
    }

    private val key: ByteArray

    init {
        key = Sha256.sha256(secret)
    }

    private val publicKeyCompress: PublicKey by lazy {
        val encoded = Secp256k1KeyPair(
            Secp256k1PrivateKey(key),
            compressed = true
        ).getPublicKey().encoded

        val csum = Ripemd160.ripemd160(encoded).copyOfRange(0, 4)
        PublicKey(
            encoded + csum
        )
    }

    // 防止外部修改私钥
    fun getKey(): ByteArray = key.copyOf()

    override fun toString(): String {
        return Base58.encodeCheck(byteArrayOf(PRIVATE_KEY_PREFIX) + key)
    }

    @Throws(EOSException.CalculatePublicKeyException::class)
    fun getPublicKey(): PublicKey {
        try {
            return publicKeyCompress
        } catch (e: JNICallException) {
            throw EOSException.CalculatePublicKeyException()
        }
    }

    @Throws(EOSException.CalculateSignatureException::class)
    fun sign(message: ByteArray): Signature {
        try {
            val secpSignature = Secp256k1Signer().sign(Secp256k1PrivateKey(getKey()), message)
            return secpSignature
        } catch (e: JNICallException) {
            throw EOSException.CalculateSignatureException()
        }
    }
}