package com.smallraw.chain.lib

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smallraw.chain.lib.bitcoin.BitcoinAddress
import com.smallraw.chain.lib.bitcoin.BitcoinKit
import com.smallraw.chain.lib.bitcoin.convert.WalletImportFormat
import com.smallraw.chain.lib.bitcoin.models.UnspentOutputWithAddress
import com.smallraw.chain.lib.bitcoin.transaction.BTCTransaction
import com.smallraw.chain.lib.bitcoin.transaction.build.*
import com.smallraw.chain.lib.bitcoin.transaction.script.Script
import com.smallraw.chain.lib.bitcoin.transaction.serializers.TransactionSerializer
import com.smallraw.chain.lib.extensions.hexStringToByteArray
import com.smallraw.chain.lib.extensions.toHex
import org.junit.Assert

import org.junit.Test
import org.junit.runner.RunWith
import java.security.PublicKey

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class BitcoinTransactionUnitTest {
    private val TAG = "BitcoinTransactionUnitTest"

    class UnitTestIPrivateKeyPairProvider(private val wifPrivate: String) :
        IPrivateKeyPairProvider {
        override fun findByPrivate(publicKey: PublicKey): Secp256k1KeyPair {
            return Secp256k1KeyPair(
                Secp256k1PrivateKey(wifPrivate.hexStringToByteArray()),
                null,
                true
            )
        }

        override fun findByAddress(address: BitcoinAddress): Secp256k1KeyPair {
            val decode =
                WalletImportFormat.decode(wifPrivate)
            return Secp256k1KeyPair(Secp256k1PrivateKey(decode.privateKey), null, true)
        }
    }

    @Test
    fun create_p2pkh_transaction_build() {
        val bitcoinKit = BitcoinKit()

        val btcTransactionBuilder = BTCTransactionBuilder(
            RecipientSetter(bitcoinKit.getAddressConverter()),
            ChangeSetter(bitcoinKit.getAddressConverter()),
            InputSetter(),
            OutputSetter(),
            BTCTransactionSigner(InputSigner(UnitTestIPrivateKeyPairProvider("cRvyLwCPLU88jsyj94L7iJjQX5C2f8koG4G2gevN4BeSGcEvfKe9")))
        )

        val build = btcTransactionBuilder.build(
            arrayListOf(
                UnspentOutputWithAddress(
                    bitcoinKit.convertAddress("myPAE9HwPeKHh8FjKwBNBaHnemApo3dw6e"),
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

        Assert.assertEquals(
            TransactionSerializer.serialize(build, false).toHex(),
            "02000000016cce96ffe999c7b2abc8b7bebec0c821e9c378ac41417106f6ddf63be2f448fb0000000000ffffffff0280969800000000001976a914fd337ad3bf81e086d96a68e1f8d6a0a510f8c24a88ac4081ba01000000001976a91442151d0c21442c2b038af0ad5ee64b9d6f4f4e4988ac00000000"
        )
        Assert.assertEquals(
            TransactionSerializer.serialize(build).toHex(),
            "02000000016cce96ffe999c7b2abc8b7bebec0c821e9c378ac41417106f6ddf63be2f448fb000000006c493044022044ef433a24c6010a90af14f7739e7c60ce2c5bc3eab96eaee9fbccfdbb3e272202205372a617cb235d0a0ec2889dbfcadf15e10890500d184c8dda90794ecdf794920000012103a2fef1829e0742b89c218c51898d9e7cb9d51201ba2bf9d9e9214ebb6af32708ffffffff0280969800000000001976a914fd337ad3bf81e086d96a68e1f8d6a0a510f8c24a88ac4081ba01000000001976a91442151d0c21442c2b038af0ad5ee64b9d6f4f4e4988ac00000000"
        )
    }
}