package com.smallraw.chain.bitcoin.transaction.build

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
class P2pkhTransferBuild {
    private val network = TestNet()
    private val bitcoinKit = BitcoinKit(network)

    private val transactionSizeCalculator = TransactionSizeCalculator()
    private val dustCalculator = DustCalculator(network.dustRelayTxFee, transactionSizeCalculator)

    private val addressConverter = AddressConverter.default(network)
    private val unspentOutputProvider = object : IUnspentOutputProvider {
        override fun nextUtxoList(): List<UnspentOutput> {
            return listOf(
                UnspentOutput(
                    addressConverter.convert("myPAE9HwPeKHh8FjKwBNBaHnemApo3dw6e"),
                    6,
                    10,
                    "a0a2eccb1b20d3779d92c671a9a01e9b64d15da3cccc1b11a68caa5b505d45dc",
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
                return Bitcoin.KeyPair.ofSecretKeyHex("81c70e36ffa5e3e6425dc19c7c35315d3d72dc60b79cb78fe009a335de29dd22")
            }

            override fun findByAddress(address: Address): Bitcoin.KeyPair {
                return Bitcoin.KeyPair.ofSecretKeyHex("81c70e36ffa5e3e6425dc19c7c35315d3d72dc60b79cb78fe009a335de29dd22")
            }
        }

    private val btcTransactionBuilder = TransactionBuilder(
        RecipientSetter(bitcoinKit.getAddressConverter()),
        ChangeSetter(bitcoinKit.getAddressConverter()),
        InputSetter(unspentOutputSelector, dustCalculator),
        OutputSetter(),
        TransactionSigner(InputSignerChain.default(privateKeyPairProvider))
    )

    @Test
    fun test_transfer() {
        val build = btcTransactionBuilder.build(
            recipientAddress = "myPAE9HwPeKHh8FjKwBNBaHnemApo3dw6e",
            recipientValue = 4000L,
            changeAddress = "myPAE9HwPeKHh8FjKwBNBaHnemApo3dw6e",
            feeRate = 1,
            senderPay = true
        )

        Assert.assertEquals(
            TransactionSerializer.serialize(build).toHex(),
            "0200000001dc455d505baa8ca6111bcccca35dd1649b1ea0a971c6929d77d3201bcbeca2a0000000006a47304402205851f1d13c95d4db92ef7d7b06dab43ffe4d66ea45685b355b549dd368d95c7502202db9491674bad24ad2ae2ca64df2ce040cc2d2d916d387fc52af8ece12a503f8012103a2fef1829e0742b89c218c51898d9e7cb9d51201ba2bf9d9e9214ebb6af32708ffffffff02a00f0000000000001976a914c3f8e5b0f8455a2b02c29c4488a550278209b66988acf9160000000000001976a914c3f8e5b0f8455a2b02c29c4488a550278209b66988ac00000000"
        )
    }
}