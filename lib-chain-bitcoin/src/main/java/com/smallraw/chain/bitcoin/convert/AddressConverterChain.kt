package com.smallraw.chain.bitcoin.convert

import com.smallraw.chain.bitcoin.Bitcoin
import com.smallraw.chain.bitcoin.execptions.AddressFormatException
import com.smallraw.chain.bitcoin.network.BaseNetwork
import com.smallraw.chain.bitcoin.network.MainNet
import com.smallraw.chain.bitcoin.transaction.script.ScriptType

class AddressConverterChain : IAddressConverter {
    companion object {
        /**
         * 默认地址转换器
         * 默认支持 base58、bech32
         */
        fun default(network: BaseNetwork = MainNet()): AddressConverterChain {
            val addressConverter = AddressConverterChain()
            addressConverter.prependConverter(
                SegwitAddressConverter(network.addressSegwitHrp)
            )
            addressConverter.prependConverter(
                Base58AddressConverter(
                    network.addressVersion,
                    network.addressScriptVersion
                )
            )
            return addressConverter
        }
    }

    private val concreteConverters = mutableListOf<IAddressConverter>()

    fun prependConverter(converter: IAddressConverter) {
        concreteConverters.add(0, converter)
    }

    override fun convert(addressString: String): Bitcoin.Address {
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
     * @param hash160Bytes hash160 后的公钥
     * @param scriptType 要产生的地址类型
     */
    override fun convert(hash160Bytes: ByteArray, scriptType: ScriptType): Bitcoin.Address {
        val exceptions = mutableListOf<Exception>()

        for (converter in concreteConverters) {
            try {
                return converter.convert(hash160Bytes, scriptType)
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
    override fun convert(publicKey: Bitcoin.PublicKey, scriptType: ScriptType): Bitcoin.Address {
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