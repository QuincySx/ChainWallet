package com.smallraw.chain.bitcoin.transaction.build

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smallraw.chain.bitcoin.Bitcoin
import com.smallraw.chain.bitcoin.BitcoinKit
import com.smallraw.chain.bitcoin.models.UnspentOutput
import com.smallraw.chain.bitcoin.transaction.DustCalculator
import com.smallraw.chain.bitcoin.transaction.TransactionSizeCalculator
import com.smallraw.chain.bitcoin.transaction.build.`interface`.ChangeSetter
import com.smallraw.chain.bitcoin.transaction.build.`interface`.IPrivateKeyPairProvider
import com.smallraw.chain.bitcoin.transaction.build.`interface`.RecipientSetter
import com.smallraw.chain.bitcoin.transaction.build.inputSigner.InputSignerChain
import com.smallraw.chain.bitcoin.unspentOutput.IUnspentOutputProvider
import com.smallraw.chain.bitcoin.unspentOutput.UnspentOutputSelector
import com.smallraw.chain.bitcoincore.PublicKey
import com.smallraw.chain.bitcoincore.address.Address
import com.smallraw.chain.bitcoincore.addressConvert.AddressConverter
import com.smallraw.chain.bitcoincore.network.TestNet
import com.smallraw.chain.bitcoincore.transaction.serializers.TransactionSerializer
import com.smallraw.crypto.core.extensions.toHex
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class P2wpkhTransferBuild {
    private val network = TestNet()
    private val bitcoinKit = BitcoinKit(network)

    private val transactionSizeCalculator = TransactionSizeCalculator()
    private val dustCalculator = DustCalculator(network.dustRelayTxFee, transactionSizeCalculator)

    private val addressConverter = AddressConverter.default(network)
    private val unspentOutputProvider = object : IUnspentOutputProvider {
        override fun nextUtxoList(): List<UnspentOutput> {
            return listOf(
                UnspentOutput(
                    addressConverter.convert("tb1qgfnqkrskfutlllfkz2whvgtrx4d6c6064wpc0t"),
                    6,
                    10,
                    "a46144a078c715fcecfc81e3c7dce359a7a40a4506e06b8dfa53acf69a60da25",
                    10000,
                    0,
                )
            )
        }
    }
    private val unspentOutputSelector =
        UnspentOutputSelector(transactionSizeCalculator, unspentOutputProvider)

    private val privateKeyPairProvider =
        object : IPrivateKeyPairProvider {
            override fun findByPublicKey(publicKey: PublicKey): Bitcoin.KeyPair {
                return Bitcoin.KeyPair.ofSecretKeyHex("0cc4bc599c758dcdcc38515f923693e04873bfcfce0a60d1ba4693ab4fbd6c89")
            }

            override fun findByAddress(address: Address): Bitcoin.KeyPair {
                return Bitcoin.KeyPair.ofSecretKeyHex("0cc4bc599c758dcdcc38515f923693e04873bfcfce0a60d1ba4693ab4fbd6c89")
            }
        }

    private val btcTransactionBuilder = TransactionBuilder(
        RecipientSetter(bitcoinKit.getAddressConverter()),
        ChangeSetter(bitcoinKit.getAddressConverter()),
        InputSetter(unspentOutputSelector, transactionSizeCalculator, dustCalculator),
        OutputSetter(),
        TransactionSigner(InputSignerChain.default(privateKeyPairProvider))
    )

    @Test
    fun test_transfer() {
        val build = btcTransactionBuilder.build(
            recipientAddress = "tb1qlr3950y08kl3merjq2tevxj6vtchzzsne7ef8pfr52fftnfyqy4q4xamq5",
            recipientValue = 5000L,
            changeAddress = "tb1qgfnqkrskfutlllfkz2whvgtrx4d6c6064wpc0t",
            feeRate = 0,
            senderPay = true
        )

        Log.e("TransactionUnitTest",TransactionSerializer.serialize(build).toHex())

        Assert.assertEquals(
            TransactionSerializer.serialize(build).toHex(),
            "0200000000010125da609af6ac53fa8d6be006450aa4a759e3dcc7e381fcecfc15c778a04461a40000000000ffffffff028813000000000000220020f8e25a3c8f3dbf1de4720297961a5a62f1710a13cfb2938523a29295cd24012a881300000000000016001442660b0e164f17fffd36129d762163355bac69fa0247304402202a2cb64cc1958149e53a6440be79b397d188657ac4c7193ced34b0c3559f70ca02202ac2929449ffb4eb3eebec537b3c832be5287bf3144b2ad55bac6bfb71dee4f801210320c0c2020719cb638180f287ca59adc61fa7c201cfba789c95176c752bef9b4e00000000"
        )
    }
}