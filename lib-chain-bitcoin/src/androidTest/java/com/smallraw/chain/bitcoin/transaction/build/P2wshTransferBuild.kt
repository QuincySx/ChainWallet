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
import com.smallraw.chain.bitcoincore.PrivateKey
import com.smallraw.chain.bitcoincore.PublicKey
import com.smallraw.chain.bitcoincore.address.Address
import com.smallraw.chain.bitcoincore.addressConvert.AddressConverter
import com.smallraw.chain.bitcoincore.network.TestNet
import com.smallraw.chain.bitcoincore.script.Chunk
import com.smallraw.chain.bitcoincore.script.OP_1
import com.smallraw.chain.bitcoincore.script.OP_2
import com.smallraw.chain.bitcoincore.script.OP_CHECKMULTISIG
import com.smallraw.chain.bitcoincore.script.Script
import com.smallraw.chain.bitcoincore.transaction.serializers.TransactionSerializer
import com.smallraw.crypto.core.extensions.hexToByteArray
import com.smallraw.crypto.core.extensions.toHex
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class P2wshTransferBuild {
    private val network = TestNet()
    private val bitcoinKit = BitcoinKit(network)

    private val transactionSizeCalculator = TransactionSizeCalculator()
    private val dustCalculator = DustCalculator(network.dustRelayTxFee, transactionSizeCalculator)

    private val addressConverter = AddressConverter.default(network)
    private val unspentOutputProvider = object : IUnspentOutputProvider {
        override fun nextUtxoList(): List<UnspentOutput> {
            val lockScript = Script(
                Chunk(OP_1),
                Chunk("0320c0c2020719cb638180f287ca59adc61fa7c201cfba789c95176c752bef9b4e".hexToByteArray()),
                Chunk("03aa49feb2409baba4c18197aaf8640d9cfd3a73aac7e4f13558017ca41bf2dd17".hexToByteArray()),
                Chunk(OP_2),
                Chunk(OP_CHECKMULTISIG),
            )

            return listOf(
                UnspentOutput(
                    addressConverter.convert("tb1qcjeue0y4fgj2hlvsx2ylgywg4ud37jeyd2acrrawvm6y2z69kgyq7nys5p"),
                    6,
                    10,
                    "daf349128c241f1cbd612594168dcde7669f86eddb7c6a139d47cb5fbb6b99f2",
                    10000,
                    1,
                    redeemScript = lockScript.scriptBytes
                )
            )
        }
    }
    private val unspentOutputSelector =
        UnspentOutputSelector(transactionSizeCalculator, unspentOutputProvider)

    private val privateKeyPairProvider =
        object : IPrivateKeyPairProvider {
            private val privateKey = mapOf(
                Pair(
                    "0320c0c2020719cb638180f287ca59adc61fa7c201cfba789c95176c752bef9b4e",
                    PrivateKey("0cc4bc599c758dcdcc38515f923693e04873bfcfce0a60d1ba4693ab4fbd6c89".hexToByteArray())
                ),
                Pair(
                    "03aa49feb2409baba4c18197aaf8640d9cfd3a73aac7e4f13558017ca41bf2dd17",
                    PrivateKey("69b33e2ee0f0cc5620df24fc804f338c8735098953387151e01903c0dada0661".hexToByteArray())
                )
            )

            override fun findByPublicKey(publicKey: PublicKey): Bitcoin.KeyPair? {
                val privateKey = privateKey[publicKey.getKey().toHex()]
                return privateKey?.getKey()?.let { Bitcoin.KeyPair.ofSecretKey(it) }
            }

            override fun findByAddress(address: Address): Bitcoin.KeyPair? {
                return null
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
            recipientAddress = "tb1qtstf97nhk2gycz7vl37esddjpxwt3ut30qp5pn",
            recipientValue = 5000L,
            changeAddress = "tb1qcjeue0y4fgj2hlvsx2ylgywg4ud37jeyd2acrrawvm6y2z69kgyq7nys5p",
            feeRate = 0,
            senderPay = true
        )

        Log.e("Transaction: ", TransactionSerializer.serialize(build).toHex())
        Assert.assertEquals(
            TransactionSerializer.serialize(build).toHex(),
            "02000000000101f2996bbb5fcb479d136a7cdbed869f66e7cd8d16942561bd1c1f248c1249f3da0100000000ffffffff0288130000000000001600145c1692fa77b2904c0bccfc7d9835b2099cb8f1718813000000000000220020c4b3ccbc954a24abfd903289f411c8af1b1f4b246abb818fae66f4450b45b2080300473044022034d55c9eb54b3ee608cad6107e80816daafa96df61c5285554e92e299b279ebf0220742f2d004363c7e83d656e082b716cd1c9cd2246b52f3dbc3dd0aa0dc112f359014751210320c0c2020719cb638180f287ca59adc61fa7c201cfba789c95176c752bef9b4e2103aa49feb2409baba4c18197aaf8640d9cfd3a73aac7e4f13558017ca41bf2dd1752ae00000000"
        )
    }
}