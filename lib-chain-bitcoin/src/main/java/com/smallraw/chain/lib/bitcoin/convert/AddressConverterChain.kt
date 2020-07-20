package com.smallraw.chain.lib.bitcoin.convert

import com.smallraw.chain.lib.bitcoin.BitcoinAddress
import com.smallraw.chain.lib.bitcoin.BitcoinPublicKey
import com.smallraw.chain.lib.bitcoin.execptions.AddressFormatException
import com.smallraw.chain.lib.bitcoin.transaction.script.ScriptType

class AddressConverterChain : IAddressConverter {
    private val concreteConverters = mutableListOf<IAddressConverter>()

    fun prependConverter(converter: IAddressConverter) {
        concreteConverters.add(0, converter)
    }

    override fun convert(addressString: String): BitcoinAddress {
        val exceptions = mutableListOf<Exception>()

        for (converter in concreteConverters) {
            try {
                return converter.convert(addressString)
            } catch (e: Exception) {
                exceptions.add(e)
            }
        }

        val exception = AddressFormatException("No converter in chain could process the address")
        exceptions.forEach {
            exception.addSuppressed(it)
        }

        throw exception
    }

    /**
     * 转换地址
     * @param bytes hash160 后的公钥
     * @param scriptType 要产生的地址类型
     */
    override fun convert(bytes: ByteArray, scriptType: ScriptType): BitcoinAddress {
        val exceptions = mutableListOf<Exception>()

        for (converter in concreteConverters) {
            try {
                return converter.convert(bytes, scriptType)
            } catch (e: Exception) {
                exceptions.add(e)
            }
        }

        val exception = AddressFormatException("No converter in chain could process the address")
        exceptions.forEach {
            exception.addSuppressed(it)
        }

        throw exception
    }

    /**
     * 转换地址
     * @param bytes 公钥
     * @param scriptType 要产生的地址类型
     */
    override fun convert(publicKey: BitcoinPublicKey, scriptType: ScriptType): BitcoinAddress {
        val exceptions = mutableListOf<Exception>()

        for (converter in concreteConverters) {
            try {
                return converter.convert(publicKey, scriptType)
            } catch (e: Exception) {
                exceptions.add(e)
            }
        }

        val exception =
            AddressFormatException("No converter in chain could process the address").also {
                exceptions.forEach { it.addSuppressed(it) }
            }

        throw exception
    }

}