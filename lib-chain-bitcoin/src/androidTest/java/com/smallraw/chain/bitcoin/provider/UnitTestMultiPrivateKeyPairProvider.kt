package com.smallraw.chain.bitcoin.provider

import com.smallraw.chain.bitcoin.Bitcoin
import com.smallraw.chain.bitcoin.convert.AddressConverterChain
import com.smallraw.chain.bitcoin.convert.WalletImportFormat
import com.smallraw.chain.bitcoin.network.TestNet
import com.smallraw.chain.lib.extensions.hexToByteArray
import com.smallraw.chain.bitcoin.transaction.build.`interface`.IPrivateKeyPairProvider
import com.smallraw.chain.bitcoin.transaction.script.ScriptType
import java.security.PublicKey

/**
 * 测试私钥提供者
 */
class UnitTestMultiPrivateKeyPairProvider(private val wifPrivates: List<String>) :
    IPrivateKeyPairProvider {

    private val privateKey: HashMap<String, Bitcoin.PrivateKey> = HashMap()

    private val addressConverterChain = AddressConverterChain.default(TestNet())

    init {
        wifPrivates.forEach {
            val decode = WalletImportFormat.decode(it)
            val keyPair = Bitcoin.KeyPair(Bitcoin.PrivateKey(decode.privateKey))
            val address = addressConverterChain.convert(keyPair.getPublicKey(), ScriptType.P2PKH)
            privateKey[address.address] = keyPair.getPrivateKey()
        }
    }

    override fun findByPrivate(publicKey: Bitcoin.PublicKey): Bitcoin.KeyPair {
        val convert = addressConverterChain.convert(publicKey, ScriptType.P2PKH)
        return Bitcoin.KeyPair(
            privateKey[convert.address],
            null,
            true
        )
    }

    override fun findByAddress(address: Bitcoin.Address): Bitcoin.KeyPair {
        return Bitcoin.KeyPair(
            privateKey[address.address],
            null,
            true
        )
    }
}