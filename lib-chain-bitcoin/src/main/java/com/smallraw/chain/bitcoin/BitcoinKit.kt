package com.smallraw.chain.bitcoin

import com.smallraw.chain.bitcoin.convert.WalletImportFormat
import com.smallraw.chain.bitcoincore.PrivateKey
import com.smallraw.chain.bitcoincore.PublicKey
import com.smallraw.chain.bitcoincore.address.Address
import com.smallraw.chain.bitcoincore.addressConvert.AddressConverter
import com.smallraw.chain.bitcoincore.network.BaseNetwork
import com.smallraw.chain.bitcoincore.network.MainNet
import com.smallraw.chain.bitcoincore.script.Script
import com.smallraw.chain.bitcoincore.script.ScriptType

open class BitcoinException : Exception() {
    // WIF 解析失败
    class WIFParsingFailedError : BitcoinException()

    // WIF 解释完成后网络不匹配
    class ResolveWIFNetworkMisFailedError : BitcoinException()
}

class BitcoinKit(
    private val network: BaseNetwork = MainNet(),
    private val isSegwit: Boolean = true
) {

    /**
     * 地址转换器
     */
    private val mAddressConverter by lazy {
        AddressConverter.default(network)
    }

    /**
     * 获取地址转换器
     */
    fun getAddressConverter() = mAddressConverter

    /**
     * 转换地址
     * @param address 比特币地址
     */
    fun convertAddress(address: String): Address {
        return mAddressConverter.convert(address)
    }

    /**
     * 转换地址
     */
    private fun convertAddress(script: Script): Address {
        return mAddressConverter.convert(
            script, if (isSegwit) {
                ScriptType.P2WSH
            } else {
                ScriptType.P2SH
            }
        )
    }

    /**
     * 转换地址
     */
    fun convertAddress(publicKey: PublicKey): Address {
        return mAddressConverter.convert(
            publicKey, if (isSegwit) {
                ScriptType.P2WPKH
            } else {
                ScriptType.P2PKH
            }
        )
    }

    /**
     * 生成私钥对
     * @param privateKey 可以不填，不填随机生成
     * @param compressed 公约压缩
     */
    fun generateKeyPair(
        privateKey: PrivateKey? = null,
        compressed: Boolean = true
    ): Bitcoin.KeyPair {
        return Bitcoin.KeyPair(privateKey, null, compressed)
    }

    /**
     * 将私钥对转换为
     */
    fun getWIFPrivate(keyPair: Bitcoin.KeyPair): String {
        return WalletImportFormat(
            network,
            keyPair.isCompress()
        ).format(keyPair.getPrivateKey().getKey())
    }

    @Throws(
        BitcoinException.WIFParsingFailedError::class,
        BitcoinException.ResolveWIFNetworkMisFailedError::class
    )

    fun getWIFPrivateKey(wif: String): Bitcoin.KeyPair {
        val decode = WalletImportFormat.decode(wif)
        if (!decode.success) {
            throw BitcoinException.WIFParsingFailedError()
        }
        if (decode.addressVersion != network.addressWifVersion) {
            throw BitcoinException.ResolveWIFNetworkMisFailedError()
        }
        return generateKeyPair(PrivateKey(decode.privateKey), decode.compressed)
    }

    fun getAddress(publicKey: PublicKey): Address {
        return convertAddress(publicKey)
    }

    fun getPayScriptAddress(script: Script): Address {
        return convertAddress(script)
    }
}