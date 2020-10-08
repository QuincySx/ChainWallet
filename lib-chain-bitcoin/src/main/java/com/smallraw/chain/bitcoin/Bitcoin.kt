package com.smallraw.chain.bitcoin

import com.smallraw.chain.bitcoin.crypto.Secp256k1Signer
import com.smallraw.chain.bitcoin.execptions.AddressFormatException
import com.smallraw.chain.bitcoin.stream.BitcoinOutputStream
import com.smallraw.chain.bitcoin.transaction.script.*
import com.smallraw.chain.lib.Secp256k1KeyPair
import com.smallraw.chain.lib.Secp256k1PrivateKey
import com.smallraw.chain.lib.Secp256k1PublicKey
import com.smallraw.chain.lib.core.crypto.Base58
import com.smallraw.chain.lib.core.crypto.DEREncode
import com.smallraw.chain.lib.core.crypto.Ripemd160
import com.smallraw.chain.lib.core.extensions.hexToByteArray
import java.math.BigInteger
import kotlin.math.min
import kotlin.random.Random

class Bitcoin {

    //region Exceptions
    open class BitcoinException : RuntimeException() {
        class PrivateKeyWrongLengthError : BitcoinException()
    }
    //endregion

    //region Address
    abstract class Address(
        val address: String,
        val hashKey: ByteArray,
        val type: AddressType
    ) {
        companion object {
            fun getNullAddress(): Address {
                return NullAddress()
            }
        }

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
//                ScriptType.P2PK -> ScriptOutputP2PK(hashKey).scriptBytes
                AddressType.P2PKH -> ScriptOutputP2PKH(hashKey).scriptBytes
                AddressType.P2SH -> ScriptOutputP2SH(hashKey).scriptBytes
                else -> throw AddressFormatException("Unknown Address Type")
            }

        fun getAddressBytes() = Base58.decode(address)
    }

    class NullAddress :
        Address("", byteArrayOf(), AddressType.P2PKH)

    /**
     * 普通地址 P2PKH、P2SH、P2PK
     */
    class LegacyAddress(addressString: String, hashKey: ByteArray, type: AddressType) :
        Address(addressString, hashKey, type)

    /**
     * 隔离见证地址 P2WPKH、P2WSH
     */
    class SegWitAddress(
        addressString: String,
        hashKey: ByteArray,
        type: AddressType,
        val version: Int
    ) : Address(addressString, hashKey, type) {
        override val lockingScript: ByteArray
            get() = OpCodes.push(version) + OpCodes.push(hashKey)
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
    open class Signature(private val byteArray: ByteArray) {
        open fun signature() = byteArray
    }

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
    //endregion
}