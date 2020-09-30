package com.smallraw.chain.lib.bitcoin

import com.smallraw.chain.lib.Secp256k1KeyPair
import com.smallraw.chain.lib.Secp256k1PrivateKey
import com.smallraw.chain.lib.Secp256k1PublicKey
import com.smallraw.chain.lib.bitcoin.execptions.AddressFormatException
import com.smallraw.chain.lib.bitcoin.transaction.script.OpCodes
import com.smallraw.chain.lib.bitcoin.transaction.script.ScriptType
import com.smallraw.chain.lib.bitcoin.transaction.script.SigHash
import com.smallraw.chain.lib.crypto.Base58
import com.smallraw.chain.lib.crypto.DEREncode
import com.smallraw.chain.lib.crypto.Ripemd160
import com.smallraw.chain.lib.crypto.Secp256k1Signer
import com.smallraw.chain.lib.extensions.hexToByteArray
import java.lang.RuntimeException
import java.math.BigInteger
import kotlin.jvm.Throws
import kotlin.math.min
import kotlin.random.Random

class Bitcoin {

    //region Exceptions
    open class BitcoinException : RuntimeException() {
        class PrivateKeyWrongLengthError : BitcoinException()
    }
    //endregion

    //region Address
    open class Address(
        val address: String,
        val hashKey: ByteArray,
        val type: AddressType
    ) {

        enum class AddressType {
            P2PKH,  // Pay to public key hash
            P2SH,   // Pay to script hash
            WITNESS // Pay to witness hash
        }

        val scriptType: ScriptType
            get() = when (type) {
                AddressType.P2PKH -> ScriptType.P2PKH
                AddressType.P2SH -> ScriptType.P2SH
                AddressType.WITNESS ->
                    if (hashKey.size == 20) ScriptType.P2WPKH else ScriptType.P2WSH
            }

        open val lockingScript: ByteArray
            get() = when (type) {
                AddressType.P2PKH -> OpCodes.p2pkhStart + OpCodes.push(hashKey) + OpCodes.p2pkhEnd
                AddressType.P2SH -> OpCodes.p2pshStart + OpCodes.push(hashKey) + OpCodes.p2pshEnd
                else -> throw AddressFormatException("Unknown Address Type")
            }

        fun getAddressBytes() = Base58.decode(address)
    }
    //endregion

    //region PublicKey
    class PublicKey(private val key: ByteArray) {
        fun getHash() = Ripemd160.hash160(key)

        fun getKey() = key

        fun scriptHashP2WPKH() = byteArrayOf()
    }
    //endregion

    //region PrivateKey
    class PrivateKey @Throws(BitcoinException.PrivateKeyWrongLengthError::class) constructor(private val key: ByteArray) {
        companion object {
            @Throws(BitcoinException.PrivateKeyWrongLengthError::class)
            fun ofNumber(num: Long): PrivateKey {
                return ofNumber(num.toBigInteger())
            }

            @Throws(BitcoinException.PrivateKeyWrongLengthError::class)
            fun ofNumber(num: BigInteger): PrivateKey {
                val priv = num.toByteArray()
                if (priv.size > 32) {
                    throw BitcoinException.PrivateKeyWrongLengthError()
                }
                val byteArray = ByteArray(32)
                System.arraycopy(priv, 0, byteArray, 32 - priv.size, min(priv.size, 32))
                return PrivateKey(byteArray)
            }

            @Throws(BitcoinException.PrivateKeyWrongLengthError::class)
            fun ofHex(hexString: String): PrivateKey {
                return PrivateKey(hexString.hexToByteArray())
            }

            @Throws(BitcoinException.PrivateKeyWrongLengthError::class)
            fun new(): PrivateKey {
                return PrivateKey(Random.Default.nextBytes(32))
            }
        }

        init {
            if (key.size != 32) {
                throw BitcoinException.PrivateKeyWrongLengthError()
            }
        }

        fun getKey(): ByteArray = key

        fun sign(message: ByteArray, sigHashValue: Byte = SigHash.ALL): Signature {
            val secpSignature = Secp256k1Signer().sign(Secp256k1PrivateKey(key), message)
            val signature = DEREncode.sigToDer(secpSignature.signature()) + sigHashValue
            return Signature(signature)
        }
    }
    //endregion

    //region KeyPair
    class KeyPair(
        privateKey: PrivateKey? = null,
        publicKey: PublicKey? = null,
        private val compressed: Boolean = true
    ) {
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

    //region Signature
    class Signature(private val byteArray: ByteArray) {
        fun signature() = byteArray
    }
    //endregion
}