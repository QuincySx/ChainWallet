package com.smallraw.chain.lib.bitcoin

import com.smallraw.chain.lib.bitcoin.convert.AddressConverterChain
import com.smallraw.chain.lib.bitcoin.convert.WalletImportFormat
import com.smallraw.chain.lib.bitcoin.network.MainNet
import com.smallraw.chain.lib.bitcoin.network.BaseNetwork
import com.smallraw.chain.lib.bitcoin.transaction.script.ScriptType
import com.smallraw.chain.lib.crypto.Ripemd160

open class BitcoinException : Exception() {
    // WIF 解析失败
    class WIFParsingFailedError : BitcoinException()

    // WIF 解释完成后网络不匹配
    class ResolveWIFNetworkMisFailedError : BitcoinException()
}

class BitcoinKit(
    private val network: BaseNetwork = MainNet()
) {

    /**
     * 地址转换器
     */
    private val mAddressConverter by lazy {
        AddressConverterChain.default()
    }

    /**
     * 获取地址转换器
     */
    fun getAddressConverter() = mAddressConverter

    /**
     * 转换地址
     * @param address 比特币地址
     */
    fun convertAddress(address: String): Bitcoin.Address {
        return mAddressConverter.convert(address)
    }

    /**
     * 转换地址
     */
    fun convertAddress(bytes: ByteArray, scriptType: ScriptType): Bitcoin.Address {
        return mAddressConverter.convert(bytes, scriptType)
    }

    /**
     * 转换地址
     */
    fun convertAddress(publicKey: Bitcoin.PublicKey, scriptType: ScriptType): Bitcoin.Address {
        return mAddressConverter.convert(publicKey, scriptType)
    }

    /**
     * 生成私钥对
     * @param privateKey 可以不填，不填随机生成
     * @param compressed 公约压缩
     */
    fun generateKeyPair(
        privateKey: Bitcoin.PrivateKey? = null,
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
    fun getKeyPairByWIF(wif: String): Bitcoin.KeyPair {
        val decode = WalletImportFormat.decode(wif)
        if (!decode.success) {
            throw BitcoinException.WIFParsingFailedError()
        }
        if (decode.addressVersion != network.addressWifVersion) {
            throw BitcoinException.ResolveWIFNetworkMisFailedError()
        }
        return generateKeyPair(Bitcoin.PrivateKey(decode.privateKey), decode.compressed)
    }

    fun getP2PKHAddress(publicKey: Bitcoin.PublicKey): Bitcoin.Address {
        val hash160 = Ripemd160.hash160(publicKey.getKey())
        return mAddressConverter.convert(hash160, ScriptType.P2PKH)
    }

    fun getP2SHAddress(publicKey: Bitcoin.PublicKey): Bitcoin.Address {
        val hash160 = Ripemd160.hash160(publicKey.getKey())
        return mAddressConverter.convert(hash160, ScriptType.P2SH)
    }
}