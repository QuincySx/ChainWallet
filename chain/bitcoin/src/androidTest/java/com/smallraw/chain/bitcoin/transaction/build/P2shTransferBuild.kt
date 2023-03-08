package com.smallraw.chain.bitcoin.transaction.build

import android.util.Log
import com.smallraw.chain.bitcoin.Bitcoin
import com.smallraw.chain.bitcoin.BitcoinKit
import com.smallraw.chain.bitcoin.models.UnspentOutput
import com.smallraw.chain.bitcoin.transaction.DustCalculator
import com.smallraw.chain.bitcoin.transaction.TransactionSizeCalculator
import com.smallraw.chain.bitcoin.transaction.build.inputSigner.InputSignerChain
import com.smallraw.chain.bitcoin.transaction.build.`interface`.ChangeSetter
import com.smallraw.chain.bitcoin.transaction.build.`interface`.IPrivateKeyPairProvider
import com.smallraw.chain.bitcoin.transaction.build.`interface`.RecipientSetter
import com.smallraw.chain.bitcoin.unspentOutput.IUnspentOutputProvider
import com.smallraw.chain.bitcoin.unspentOutput.UnspentOutputSelector
import com.smallraw.chain.bitcoincore.PrivateKey
import com.smallraw.chain.bitcoincore.PublicKey
import com.smallraw.chain.bitcoincore.address.Address
import com.smallraw.chain.bitcoincore.addressConvert.AddressConverter
import com.smallraw.chain.bitcoincore.network.TestNet
import com.smallraw.chain.bitcoincore.script.*
import com.smallraw.chain.bitcoincore.transaction.serializers.TransactionSerializer
import com.smallraw.crypto.core.extensions.hexToByteArray
import com.smallraw.crypto.core.extensions.toHex
import org.junit.Assert
import org.junit.Test


class P2shTransferBuild {
    private val network = TestNet()
    private val bitcoinKit = BitcoinKit(network)

    private val transactionSizeCalculator = TransactionSizeCalculator()
    private val dustCalculator = DustCalculator(network.dustRelayTxFee, transactionSizeCalculator)

    private val addressConverter = AddressConverter.default(network)
    private val unspentOutputProvider = object : IUnspentOutputProvider {
        override fun nextUtxoList(): List<UnspentOutput> {
            val lockScript = Script(
                Chunk(OP_2),
                Chunk("03d728ad6757d4784effea04d47baafa216cf474866c2d4dc99b1e8e3eb936e730".hexToByteArray()),
                Chunk("02d83bba35a8022c247b645eed6f81ac41b7c1580de550e7e82c75ad63ee9ac2fd".hexToByteArray()),
                Chunk("03aeb681df5ac19e449a872b9e9347f1db5a0394d2ec5caf2a9c143f86e232b0d9".hexToByteArray()),
                Chunk(OP_3),
                Chunk(OP_CHECKMULTISIG),
            )

            return listOf(
                UnspentOutput(
                    addressConverter.convert("2NDa869NiSMSGd8tFe2QVLfLWXYxXNSzN6c"),
                    6,
                    10,
                    "032da8f4b94015ef84e758719e8cf98f636409fbcbb2fd95bfae2e1faa4ad77a",
                    10000,
                    0,
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
                    "03d728ad6757d4784effea04d47baafa216cf474866c2d4dc99b1e8e3eb936e730",
                    PrivateKey("471817cfb2eedbc9a3e10b89d5fc3e8a5cdbc13fe58f08c6d9013df5060b2a3c".hexToByteArray())
                ),
                Pair(
                    "02d83bba35a8022c247b645eed6f81ac41b7c1580de550e7e82c75ad63ee9ac2fd",
                    PrivateKey("71516154a21724d57fdcdb242cca34a324ffff9b3e4befcc7f375bc5e896250f".hexToByteArray())
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
        InputSetter(unspentOutputSelector, dustCalculator),
        OutputSetter(),
        TransactionSigner(InputSignerChain.default(privateKeyPairProvider))
    )

    @Test
    fun test_transfer() {
        val build = btcTransactionBuilder.build(
            recipientAddress = "myPAE9HwPeKHh8FjKwBNBaHnemApo3dw6e",
            recipientValue = 9000L,
            feeRate = 0,
            senderPay = true
        )

        Log.e("Transaction: ", TransactionSerializer.serialize(build).toHex())
        Assert.assertEquals(
            TransactionSerializer.serialize(build).toHex(),
            "02000000017ad74aaa1f2eaebf95fdb2cbfb0964638ff98c9e7158e784ef1540b9f4a82d0300000000fdfe0000483045022100a6a432163aa421d9823104b630f60a4c61f9f9455e50bda00a060b1e6c8ee10502206ae3b4f92ec84cc694542f11f127d9216d96cab683e9d66c88d6b679191c4f7e01483045022100a48b2acaae471a6a9f6e51396b40ad09aaabba4c602457d7efa5dd74aa6640750220288eb7185e43a3a1e3a2be2c2a2ccf4d0a3852a1529d22a89e1cde28c98169aa014c69522103d728ad6757d4784effea04d47baafa216cf474866c2d4dc99b1e8e3eb936e7302102d83bba35a8022c247b645eed6f81ac41b7c1580de550e7e82c75ad63ee9ac2fd2103aeb681df5ac19e449a872b9e9347f1db5a0394d2ec5caf2a9c143f86e232b0d953aeffffffff0128230000000000001976a914c3f8e5b0f8455a2b02c29c4488a550278209b66988ac00000000"
        )
    }
}