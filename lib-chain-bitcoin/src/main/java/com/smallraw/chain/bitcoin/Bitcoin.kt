package com.smallraw.chain.bitcoin

import com.smallraw.chain.bitcoincore.PrivateKey
import com.smallraw.chain.bitcoincore.PublicKey
import com.smallraw.chain.bitcoincore.Signature
import com.smallraw.chain.bitcoincore.stream.BitcoinOutputStream
import com.smallraw.crypto.Secp256k1KeyPair
import com.smallraw.crypto.Secp256k1PrivateKey
import com.smallraw.crypto.Secp256k1PublicKey

class Bitcoin {

    //region Exceptions
    open class BitcoinException : RuntimeException() {
        class PrivateKeyWrongLengthError : BitcoinException()
    }
    //endregion

    class MultiSignature(private val signatures: List<Signature>) : Signature(byteArrayOf()) {
        override fun signature(): ByteArray {
            val stream = BitcoinOutputStream()
            signatures.forEach {
                stream.writeBytes(it.signature())
            }
            return stream.toByteArray()
        }

        fun getSignatures() = signatures

        fun signSize() = signatures.size
    }

    //region KeyPair
    class KeyPair(
        privateKey: PrivateKey? = null,
        publicKey: PublicKey? = null,
        private val compressed: Boolean = true
    ) {
        companion object {
            fun ofSecretKey(secretKey: ByteArray): KeyPair {
                return KeyPair(PrivateKey(secretKey))
            }

            fun ofSecretKeyHex(secretKeyHex: String): KeyPair {
                return KeyPair(PrivateKey.ofHex(secretKeyHex))
            }
        }

        private val mSecp256k1KeyPair by lazy {
            Secp256k1KeyPair(
                privateKey?.getKey()?.let { Secp256k1PrivateKey(it) },
                publicKey?.getKey()?.let { Secp256k1PublicKey(it) },
                compressed
            )
        }

        fun getPrivateKey(): PrivateKey {
            return PrivateKey(mSecp256k1KeyPair.getPrivateKey().encoded)
        }

        fun getPublicKey(): PublicKey {
            return PublicKey(mSecp256k1KeyPair.getPublicKey().encoded)
        }

        fun isCompress() = compressed
    }
    //endregion
}