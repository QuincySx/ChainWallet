package com.smallraw.chain.bitcoincore.addressConvert

import com.smallraw.chain.bitcoincore.PublicKey
import com.smallraw.chain.bitcoincore.address.Address
import com.smallraw.chain.bitcoincore.execptions.BitcoinException
import com.smallraw.chain.bitcoincore.network.BaseNetwork
import com.smallraw.chain.bitcoincore.network.MainNet
import com.smallraw.chain.bitcoincore.script.Script
import com.smallraw.chain.bitcoincore.script.ScriptType

class AddressConverter : IAddressConverter {
    companion object {
        /**
         * 默认地址转换器
         * 默认支持 base58、bech32
         * @param 运行的网络环境
         */
        @JvmStatic
        fun default(network: BaseNetwork = MainNet()): AddressConverter {
            val addressConverter = AddressConverter()
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

    @Throws(BitcoinException::class)
    override fun convert(addressString: String): Address {
        val exceptions = mutableListOf<Exception>()

        for (converter in concreteConverters) {
            try {
                return converter.convert(addressString)
            } catch (e: Exception) {
                exceptions.add(e)
            }
        }

        val exception =
            BitcoinException(
                BitcoinException.ERR_ADDRESS_BAD_FORMAT,
                "No converter in chain could process the address"
            )
        exceptions.forEach {
            exception.addSuppressed(it)
        }

        throw exception
    }

    /**
     * 转换地址
     * @param hashBytes hash160 后的公钥
     * @param scriptType 要产生的地址类型
     */
    @Throws(BitcoinException::class)
    override fun convert(hashBytes: ByteArray, scriptType: ScriptType): Address {
        val exceptions = mutableListOf<Exception>()

        for (converter in concreteConverters) {
            try {
                return converter.convert(hashBytes, scriptType)
            } catch (e: Exception) {
                exceptions.add(e)
            }
        }

        val exception =
            BitcoinException(
                BitcoinException.ERR_ADDRESS_BAD_FORMAT,
                "No converter in chain could process the address"
            )
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
    @Throws(BitcoinException::class)
    override fun convert(publicKey: PublicKey, scriptType: ScriptType): Address {
        val exceptions = mutableListOf<Exception>()

        for (converter in concreteConverters) {
            try {
                return converter.convert(publicKey, scriptType)
            } catch (e: Exception) {
                exceptions.add(e)
            }
        }
        val exception = BitcoinException(
            BitcoinException.ERR_ADDRESS_BAD_FORMAT,
            "No converter in chain could process the address"
        ).also {
            exceptions.forEach { it.addSuppressed(it) }
        }
        throw exception
    }

    /**
     * 转换地址，此方法比较灵活。
     * @param script 脚本
     * @param scriptType 要产生的地址类型
     */
    @Throws(BitcoinException::class)
    override fun convert(script: Script, scriptType: ScriptType): Address {
        val exceptions = mutableListOf<Exception>()

        for (converter in concreteConverters) {
            try {
                return converter.convert(script, scriptType)
            } catch (e: Exception) {
                exceptions.add(e)
            }
        }
        val exception = BitcoinException(
            BitcoinException.ERR_ADDRESS_BAD_FORMAT,
            "No converter in chain could process the address"
        ).also {
            exceptions.forEach { it.addSuppressed(it) }
        }
        throw exception
    }
}