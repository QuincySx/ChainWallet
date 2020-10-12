package com.smallraw.chain.bitcoin

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smallraw.chain.bitcoin.convert.WalletImportFormat
import com.smallraw.chain.bitcoin.models.UnspentOutputWithAddress
import com.smallraw.chain.bitcoin.provider.UnitTestMultiPrivateKeyPairProvider
import com.smallraw.chain.bitcoin.provider.UnitTestPrivateKeyPairProvider
import com.smallraw.chain.bitcoin.transaction.build.InputSetter
import com.smallraw.chain.bitcoin.transaction.build.InputSigner
import com.smallraw.chain.bitcoin.transaction.build.OutputSetter
import com.smallraw.chain.bitcoin.transaction.build.TransactionBuilder
import com.smallraw.chain.bitcoin.transaction.build.TransactionSigner
import com.smallraw.chain.bitcoin.transaction.build.`interface`.ChangeSetter
import com.smallraw.chain.bitcoin.transaction.build.`interface`.RecipientSetter
import com.smallraw.chain.bitcoincore.PrivateKey
import com.smallraw.chain.bitcoincore.network.MainNet
import com.smallraw.chain.bitcoincore.network.TestNet
import com.smallraw.chain.bitcoincore.script.Chunk
import com.smallraw.chain.bitcoincore.script.OP_CHECKMULTISIG
import com.smallraw.chain.bitcoincore.script.ScriptType
import com.smallraw.chain.bitcoincore.script.toScriptBytes
import com.smallraw.chain.bitcoincore.transaction.serializers.TransactionSerializer
import com.smallraw.crypto.core.crypto.Ripemd160
import com.smallraw.crypto.core.extensions.hexToByteArray
import com.smallraw.crypto.core.extensions.toHex
import org.junit.Assert
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
        val bitcoinKit = BitcoinKit(TestNet())

        val btcTransactionBuilder = TransactionBuilder(
            RecipientSetter(bitcoinKit.getAddressConverter()),
            ChangeSetter(bitcoinKit.getAddressConverter()),
            InputSetter(),
            OutputSetter(),
            TransactionSigner(InputSigner(UnitTestPrivateKeyPairProvider("cRvyLwCPLU88jsyj94L7iJjQX5C2f8koG4G2gevN4BeSGcEvfKe9")))
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
            recipientAddress = "n4bkvTyU1dVdzsrhWBqBw8fEMbHjJvtmJR",
            recipientValue = 10000000,
            changeAddress = "mmYNBho9BWQB2dSniP1NJvnPoj5EVWw89w",
            changeValue = 29000000
        ).build()

        Assert.assertEquals(
            TransactionSerializer.serialize(build, false).toHex(),
            "02000000016cce96ffe999c7b2abc8b7bebec0c821e9c378ac41417106f6ddf63be2f448fb0000000000ffffffff0280969800000000001976a914fd337ad3bf81e086d96a68e1f8d6a0a510f8c24a88ac4081ba01000000001976a91442151d0c21442c2b038af0ad5ee64b9d6f4f4e4988ac00000000"
        )
        Assert.assertEquals(
            TransactionSerializer.serialize(build).toHex(),
            "02000000016cce96ffe999c7b2abc8b7bebec0c821e9c378ac41417106f6ddf63be2f448fb000000006a473044022044ef433a24c6010a90af14f7739e7c60ce2c5bc3eab96eaee9fbccfdbb3e272202205372a617cb235d0a0ec2889dbfcadf15e10890500d184c8dda90794ecdf79492012103a2fef1829e0742b89c218c51898d9e7cb9d51201ba2bf9d9e9214ebb6af32708ffffffff0280969800000000001976a914fd337ad3bf81e086d96a68e1f8d6a0a510f8c24a88ac4081ba01000000001976a91442151d0c21442c2b038af0ad5ee64b9d6f4f4e4988ac00000000"
        )
    }

    @Test
    fun create_p2sh_transaction_build() {
        val bitcoinKit = BitcoinKit(TestNet())

        val btcTransactionBuilder = TransactionBuilder(
            RecipientSetter(bitcoinKit.getAddressConverter()),
            ChangeSetter(bitcoinKit.getAddressConverter()),
            InputSetter(),
            OutputSetter(),
            TransactionSigner(
                InputSigner(
                    UnitTestMultiPrivateKeyPairProvider(
                        listOf(
                            "cRvyLwCPLU88jsyj94L7iJjQX5C2f8koG4G2gevN4BeSGcEvfKe9",
                            "cSrLXdF2fuLGBUuYMK3AiFdw9uidzxVno53SNpcYvDTiG22G6bZC",
                            "cSW3jW8BgqmUAYfRA7PmgA13TScpX64XozmsAP6Thn1ERdgQSmoY"
                        )
                    )
                )
            )
        )

        val publicKey1 =
            Bitcoin.KeyPair(PrivateKey(WalletImportFormat.decode("cRvyLwCPLU88jsyj94L7iJjQX5C2f8koG4G2gevN4BeSGcEvfKe9").privateKey))
                .getPublicKey().getKey()
        val publicKey2 =
            Bitcoin.KeyPair(PrivateKey(WalletImportFormat.decode("cSrLXdF2fuLGBUuYMK3AiFdw9uidzxVno53SNpcYvDTiG22G6bZC").privateKey))
                .getPublicKey().getKey()
        val publicKey3 =
            Bitcoin.KeyPair(PrivateKey(WalletImportFormat.decode("cSW3jW8BgqmUAYfRA7PmgA13TScpX64XozmsAP6Thn1ERdgQSmoY").privateKey))
                .getPublicKey().getKey()

        Log.e("p2shAddress publicKey1", publicKey1.toHex())
        Log.e("p2shAddress publicKey2", publicKey2.toHex())
        Log.e("p2shAddress publicKey3", publicKey3.toHex())

        // 2 <Public Key A> <Public Key B> <Public Key C> 3 OP_CHECKMULTISIG
        val scriptBytes = listOf(
            Chunk(1),//OP_1
            Chunk(publicKey1),
            Chunk(publicKey2),
            Chunk(publicKey3),
            Chunk(3),//OP_3
            Chunk(OP_CHECKMULTISIG),
        ).toScriptBytes()

        Log.e("p2shAddress Redemption Script", scriptBytes.toHex())

        val convertAddress =
            bitcoinKit.convertAddress(Ripemd160.hash160(scriptBytes), ScriptType.P2SH)

        Log.e("p2shAddress address", convertAddress.toString())

        val build = btcTransactionBuilder.build(
            arrayListOf(
                UnspentOutputWithAddress(
                    bitcoinKit.convertAddress("2NDjq2yVbP1MBMsVZXKxDkGNTVEFGvm6tMG"),
                    6,
                    10000,
                    "fb48f4e23bf6ddf606714141ac78c3e921c8c0bebeb7c8abb2c799e9ff96ce6c",
                    "29000000",
                    0
                )
            ),
            recipientAddress = "n4bkvTyU1dVdzsrhWBqBw8fEMbHjJvtmJR",
            recipientValue = 10000000,
            changeAddress = "2NDjq2yVbP1MBMsVZXKxDkGNTVEFGvm6tMG",
            changeValue = 29000000
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

    @Test
    fun create_p2sh1_transaction_build() {
        val bitcoinKit = BitcoinKit(MainNet())

        val btcTransactionBuilder = TransactionBuilder(
            RecipientSetter(bitcoinKit.getAddressConverter()),
            ChangeSetter(bitcoinKit.getAddressConverter()),
            InputSetter(),
            OutputSetter(),
            TransactionSigner(
                InputSigner(
                    UnitTestMultiPrivateKeyPairProvider(
                        listOf(
                            "KybuecAGpGhfLP4y6bd6bidFn23dGK2EJJi8zvbwjoffYd14EsU6",
                            "L11z9LhtCJmPPtK4cwMC4s9s9R3uXkuPkmGfjBmUGGHn7eFejiPC"
                        )
                    )
                )
            )
        )

        val publicKey1 =
            "03d728ad6757d4784effea04d47baafa216cf474866c2d4dc99b1e8e3eb936e730".hexToByteArray()
        val publicKey2 =
            "02d83bba35a8022c247b645eed6f81ac41b7c1580de550e7e82c75ad63ee9ac2fd".hexToByteArray()
        val publicKey3 =
            "03aeb681df5ac19e449a872b9e9347f1db5a0394d2ec5caf2a9c143f86e232b0d9".hexToByteArray()

        Log.e("p2shAddress publicKey1", publicKey1.toHex())
        Log.e("p2shAddress publicKey2", publicKey2.toHex())
        Log.e("p2shAddress publicKey3", publicKey3.toHex())

        // 2 <Public Key A> <Public Key B> <Public Key C> 3 OP_CHECKMULTISIG
        val scriptBytes = listOf(
            Chunk(2),//OP_2
            Chunk(publicKey1),
            Chunk(publicKey2),
            Chunk(publicKey3),
            Chunk(3),//OP_3
            Chunk(OP_CHECKMULTISIG),
        ).toScriptBytes()

        Log.e("p2shAddress Redemption Script", scriptBytes.toHex())

        val convertAddress =
            bitcoinKit.convertAddress(Ripemd160.hash160(scriptBytes), ScriptType.P2SH)

        Log.e("p2shAddress address", convertAddress.toString())

        // 4104184f32b212815c6e522e66686324030ff7e5bf08efb21f8b00614fb7690e19131dd31304c54f37baa40db231c918106bb9fd43373e37ae31a0befc6ecaefb867ac
        val build = btcTransactionBuilder.build(
            arrayListOf(
                UnspentOutputWithAddress(
                    bitcoinKit.convertAddress("3N1v2QSgptvvRMFhxtnciiMFKCkMcQiWcy"),
                    177254,
                    10000,
                    "7a06ea98cd40ba2e3288262b28638cec5337c1456aaf5eedc8e9e5a20f062bdf",
                    "5000000000",
                    0,
                    scriptBytes
                )
            ),
            recipientAddress = "1Ce8WxgwjarzLtV6zkUGgdwmAe5yjHoPXX",
            recipientValue = 4999950000,
            version = 1
        ).build()

//        Assert.assertEquals(
//            TransactionSerializer.serialize(build, false).toHex(),
//            "02000000016cce96ffe999c7b2abc8b7bebec0c821e9c378ac41417106f6ddf63be2f448fb0000000000ffffffff0280969800000000001976a914fd337ad3bf81e086d96a68e1f8d6a0a510f8c24a88ac4081ba01000000001976a91442151d0c21442c2b038af0ad5ee64b9d6f4f4e4988ac00000000"
//        )

        Log.e("p2shAddress Transaction", TransactionSerializer.serialize(build).toHex())
        Assert.assertEquals(
            TransactionSerializer.serialize(build).toHex(),
            "01000000017a06ea98cd40ba2e3288262b28638cec5337c1456aaf5eedc8e9e5a20f062bdf00000000fdfe0000493046022100c78dc4c472e79f6189ab8f66803a46c17a8e6cd24b88f11892b9bff9fd9cdd69022100ef7243c0bbbaa007dafb05047c84767721ac20cec18c27b423c4066e468a2dc50147304402205fa58d3d38fbc19f6139743c724b86fd1c1964ea785b70cd8be4fddd614059c8022055cbbea69c853e8b61035007b49dceecbf8606c1382f747acc7bd9462f76220e014c69522103d728ad6757d4784effea04d47baafa216cf474866c2d4dc99b1e8e3eb936e7302102d83bba35a8022c247b645eed6f81ac41b7c1580de550e7e82c75ad63ee9ac2fd2103aeb681df5ac19e449a872b9e9347f1db5a0394d2ec5caf2a9c143f86e232b0d953aeffffffff01b02e052a010000001976a9147faf0c785828c1f87fca32ef071066f60ea100d188ac00000000"
        )
    }
}