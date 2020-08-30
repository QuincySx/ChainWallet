package com.smallraw.chain.lib.bitcoin

import com.smallraw.chain.lib.Secp256k1KeyPair
import com.smallraw.chain.lib.Secp256k1PrivateKey
import com.smallraw.chain.lib.bitcoin.convert.AddressConverterChain
import com.smallraw.chain.lib.bitcoin.convert.Base58AddressConverter
import com.smallraw.chain.lib.bitcoin.convert.WalletImportFormat
import com.smallraw.chain.lib.bitcoin.network.Network
import com.smallraw.chain.lib.bitcoin.network.TestNet
import com.smallraw.chain.lib.bitcoin.transaction.script.ScriptType
import com.smallraw.chain.lib.crypto.Ripemd160
import com.smallraw.chain.lib.crypto.Secp256k1Signer
import java.security.PrivateKey
import java.security.PublicKey

open class BitcoinException : Exception() {
    // WIF 解析失败
    class WIFParsingFailedError : BitcoinException()

    // WIF 解释完成后网络不匹配
    class ResolveWIFNetworkMisFailedError : BitcoinException()
}

class BitcoinKit(
    private val network: Network = TestNet()
) {
    /**
     * 签名器
     */
    val mSigner by lazy {
        Secp256k1Signer()
    }

    /**
     * 地址转换器
     */
    private val mAddressConverter by lazy {
        val addressConverter = AddressConverterChain()
        addressConverter.prependConverter(
            Base58AddressConverter(
                network.addressVersion,
                network.addressScriptVersion
            )
        )
        addressConverter
    }

    /**
     * 获取地址转换器
     */
    fun getAddressConverter() = mAddressConverter

    /**
     * 转换地址
     */
    fun convertAddress(address: String): BitcoinAddress {
        return mAddressConverter.convert(address)
    }

    /**
     * 转换地址
     */
    fun convertAddress(bytes: ByteArray, scriptType: ScriptType): BitcoinAddress {
        return mAddressConverter.convert(bytes, scriptType)
    }

    /**
     * 转换地址
     */
    fun convertAddress(publicKey: BitcoinPublicKey, scriptType: ScriptType): BitcoinAddress {
        return mAddressConverter.convert(publicKey, scriptType)
    }

    /**
     * 生成私钥对
     * @param privateKey 可以不填，不填随机生成
     * @param compressed 公约压缩
     */
    fun generateKeyPair(
        privateKey: PrivateKey? = null,
        compressed: Boolean = true
    ): Secp256k1KeyPair {
        return Secp256k1KeyPair(privateKey, null, compressed)
    }

    /**
     * 将私钥对转换为
     */
    fun getWIFPrivate(keyPair: Secp256k1KeyPair): String {
        return WalletImportFormat(
            network,
            keyPair.compressed
        ).format(keyPair.getPrivateKey().encoded)
    }

    @Throws(
        BitcoinException.WIFParsingFailedError::class,
        BitcoinException.ResolveWIFNetworkMisFailedError::class
    )
    fun getKeyPairByWIF(wif: String): Secp256k1KeyPair {
        val decode = WalletImportFormat.decode(wif)
        if (!decode.success) {
            throw BitcoinException.WIFParsingFailedError()
        }
        if (decode.addressVersion != network.addressWifVersion) {
            throw BitcoinException.ResolveWIFNetworkMisFailedError()
        }
        return generateKeyPair(Secp256k1PrivateKey(decode.privateKey), decode.compressed)
    }

    fun getP2PKHAddress(publicKey: PublicKey): BitcoinAddress {
        val hash160 = Ripemd160.hash160(publicKey.encoded)
        return mAddressConverter.convert(hash160, ScriptType.P2PKH)
    }

    fun getP2SHAddress(publicKey: PublicKey): BitcoinAddress {
        val hash160 = Ripemd160.hash160(publicKey.encoded)
        return mAddressConverter.convert(hash160, ScriptType.P2SH)
    }
}