package com.smallraw.chain.lib.bitcoin

import com.smallraw.chain.lib.bitcoin.execptions.AddressFormatException
import com.smallraw.chain.lib.bitcoin.transaction.script.OpCodes
import com.smallraw.chain.lib.bitcoin.transaction.script.ScriptType
import com.smallraw.chain.lib.crypto.Base58
import com.smallraw.chain.lib.extensions.toHex
import java.lang.RuntimeException
import java.math.BigInteger
import kotlin.math.min
import kotlin.random.Random

class BitcoinTest {
    open class BitcoinError : RuntimeException()
    class PrivateKeyWrongLengthError : BitcoinError()

    enum class AddressType {
        P2PKH,  // Pay to public key hash
        P2SH,   // Pay to script hash
        WITNESS // Pay to witness hash
    }

    //region Address
    open class Address(
        protected val address: String,
        val hashKey: ByteArray,
        val type: AddressType
    ) {
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

        fun getAddressBytes() = address

        fun getAddress() = Base58.decode(address)
    }
    //endregion


    //region PublicKey
    interface PublicKey {
        fun getHash(): ByteArray

        fun getKey(): ByteArray

        fun scriptHashP2WPKH(): ByteArray
    }
    //endregion

    //region PrivateKey
    class PrivateKey(private val key: ByteArray = Random.Default.nextBytes(32)) {
        companion object {
            fun of(num: Long): PrivateKey {
                return of(num.toBigInteger())
            }

            fun of(num: BigInteger): PrivateKey {
                val priv = num.toByteArray()
                val byteArray = ByteArray(32)
                System.arraycopy(priv, 0, byteArray, 0, min(priv.size, 32))
                return PrivateKey(byteArray)
            }
        }

        init {
            if (key.size != 32) {
                throw PrivateKeyWrongLengthError()
            }
        }

        fun getKey(): ByteArray = key

        fun getKeyFormat(): String = key.toHex()

        fun sign(message: ByteArray): ByteArray {
            return byteArrayOf()
        }
    }
    //endregion
}

fun main() {

}