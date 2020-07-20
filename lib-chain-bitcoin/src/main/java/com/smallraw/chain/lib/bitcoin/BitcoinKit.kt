package com.smallraw.chain.lib.bitcoin

import com.smallraw.chain.lib.Secp256k1KeyPair
import com.smallraw.chain.lib.bitcoin.convert.AddressConverterChain
import com.smallraw.chain.lib.bitcoin.convert.Base58AddressConverter
import com.smallraw.chain.lib.bitcoin.convert.WalletImportFormat
import com.smallraw.chain.lib.bitcoin.network.Network
import com.smallraw.chain.lib.bitcoin.network.TestNet
import com.smallraw.chain.lib.bitcoin.transaction.script.ScriptType
import com.smallraw.chain.lib.crypto.Ripemd160
import com.smallraw.chain.lib.crypto.Secp256k1Signer
import java.security.PrivateKey

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
    val mAddressConverter by lazy {
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

    fun getP2PKHAddress(keyPair: Secp256k1KeyPair): BitcoinAddress {
        val hash160 = Ripemd160.hash160(keyPair.getPublicKey().encoded)
        return mAddressConverter.convert(hash160, ScriptType.P2PKH)
    }

    fun getP2SHAddress(keyPair: BitcoinPublicKey): BitcoinAddress {
        return mAddressConverter.convert(keyPair, ScriptType.P2SH)
    }
}