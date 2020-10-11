package com.smallraw.chain.bitcoin.provider

import com.smallraw.chain.bitcoin.Bitcoin
import com.smallraw.chain.bitcoin.convert.WalletImportFormat
import com.smallraw.chain.bitcoin.transaction.build.`interface`.IPrivateKeyPairProvider
import com.smallraw.chain.bitcoincore.PrivateKey
import com.smallraw.chain.bitcoincore.PublicKey
import com.smallraw.chain.bitcoincore.address.Address
import com.smallraw.chain.bitcoincore.addressConvert.AddressConverter
import com.smallraw.chain.bitcoincore.network.TestNet
import com.smallraw.chain.bitcoincore.script.ScriptType

/**
 * 测试私钥提供者
 */
class UnitTestMultiPrivateKeyPairProvider(private val wifPrivates: List<String>) :
    IPrivateKeyPairProvider {

    private val privateKey: HashMap<String, PrivateKey> = HashMap()

    private val addressConverterChain = AddressConverter.default(TestNet())

    init {
        wifPrivates.forEach {
            val decode = WalletImportFormat.decode(it)
            val keyPair = Bitcoin.KeyPair.ofSecretKey(decode.privateKey)
            val address = addressConverterChain.convert(keyPair.getPublicKey(), ScriptType.P2PKH)
            privateKey[address.toString()] = keyPair.getPrivateKey()
        }
    }

    override fun findByPrivate(publicKey: PublicKey): Bitcoin.KeyPair {
        val convert = addressConverterChain.convert(publicKey, ScriptType.P2PKH)
        return Bitcoin.KeyPair(
            privateKey[convert.toString()],
            null,
            true
        )
    }

    override fun findByAddress(address: Address): Bitcoin.KeyPair {
        return Bitcoin.KeyPair(
            privateKey[address.toString()],
            null,
            true
        )
    }
}