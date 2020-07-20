package com.smallraw.chain.lib

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smallraw.chain.lib.bitcoin.BitcoinP2PKHAddress
import com.smallraw.chain.lib.bitcoin.convert.AddressConverterChain
import com.smallraw.chain.lib.bitcoin.convert.Base58AddressConverter
import com.smallraw.chain.lib.bitcoin.models.UnspentOutputWithAddress
import com.smallraw.chain.lib.bitcoin.transaction.BTCTransaction
import com.smallraw.chain.lib.bitcoin.transaction.build.*
import com.smallraw.chain.lib.bitcoin.transaction.script.Script
import com.smallraw.chain.lib.bitcoin.transaction.serializers.TransactionSerializer
import com.smallraw.chain.lib.extensions.hexStringToByteArray
import com.smallraw.chain.lib.extensions.toHex

import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class BitcoinTransactionUnitTest {
    private val TAG = "BitcoinTransactionUnitTest"

    @Test
    fun create_p2pkh_transaction_build() {
        val addressConverterChain = AddressConverterChain()
        addressConverterChain.prependConverter(Base58AddressConverter(111, 196))

        val btcTransactionBuilder = BTCTransactionBuilder(
            RecipientSetter(addressConverterChain),
            ChangeSetter(addressConverterChain),
            InputSetter(),
            OutputSetter(),
            BTCTransactionSigner(InputSigner(PrivateKeyProvider()))
//            EmptyBTCTransactionSigner()
        )
        val build = btcTransactionBuilder.build(
            arrayListOf(
                UnspentOutputWithAddress(
                    BitcoinP2PKHAddress.fromAddress("myPAE9HwPeKHh8FjKwBNBaHnemApo3dw6e"),
                    6,
                    10000,
                    "fb48f4e23bf6ddf606714141ac78c3e921c8c0bebeb7c8abb2c799e9ff96ce6c",
                    "29000000",
                    0
                )
            ),
            "n4bkvTyU1dVdzsrhWBqBw8fEMbHjJvtmJR",
            10000000,
            "mmYNBho9BWQB2dSniP1NJvnPoj5EVWw89w",
            29000000
        ).build()

        Log.e(TAG, TransactionSerializer.serialize(build).toHex())
    }

    @Test
    fun create_p2pkh_transaction() {
        val input = BTCTransaction.Input(
            "fb48f4e23bf6ddf606714141ac78c3e921c8c0bebeb7c8abb2c799e9ff96ce6c".hexStringToByteArray(),
            0
        )

        val fromAddress = BitcoinP2PKHAddress.fromAddress("n4bkvTyU1dVdzsrhWBqBw8fEMbHjJvtmJR")

        val txoutScript = Script.scriptP2PKH(fromAddress.hashKey)

        val txout = BTCTransaction.Output(10000000, txoutScript)

        val toAddress = BitcoinP2PKHAddress.fromAddress("mmYNBho9BWQB2dSniP1NJvnPoj5EVWw89w")

        val change_txout = Script.scriptP2PKH(toAddress.hashKey)

        val output = BTCTransaction.Output(29000000, change_txout)

        val tx = BTCTransaction(
            inputs = arrayOf(input), outputs = arrayOf(txout, output)
        )

        Log.e(TAG, TransactionSerializer.serialize(tx).toHex())
//        val privateKey =
//            "74e5eb5e87a7eca6f3d9142fcbf26858fe75e57261df60208e97543222906b33".hexStringToByteArray()
//        val bitcoinAccount = BitcoinAccount(
//            Secp256k1PrivateKey(
//                privateKey
//            ), testNet = false
//        )
//        val wifPrivateKey = bitcoinAccount.getWifPrivateKey()
//        val publicKey = bitcoinAccount.getPublicKey().format
//        val address = bitcoinAccount.getAddress().getFormat()
//        val rawAddress = bitcoinAccount.getAddress().getAddress().toHex()
//
//        Log.e(TAG, wifPrivateKey)
//        Log.e(TAG, publicKey)
//        Log.e(TAG, address)
//        Log.e(TAG, rawAddress)
//
//        assertEquals(wifPrivateKey, "L18wo72G3Y4tcp8BqJEa6uqDCH6VbsewmYGkx54nsZhs6kmt9h5F")
//        assertEquals(
//            publicKey,
//            "02e696c8a8d35a1c5f0e6a1f345424c34ed39f0e50195d7183cdf45cd237b2b96f"
//        )
//        assertEquals(rawAddress, "00298657ffb809d64076c585dd67fd80753d470619b05d72c3")
//        assertEquals(address, "14nZeETvctxC6w3gdJMGL6ZtvDq79JtPhL")
    }
}