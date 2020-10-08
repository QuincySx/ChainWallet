package com.smallraw.chain.bitcoincore.transaction

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smallraw.chain.bitcoincore.PrivateKey
import com.smallraw.chain.bitcoincore.addressConvert.AddressConverter
import com.smallraw.chain.bitcoincore.network.TestNet
import com.smallraw.chain.bitcoincore.script.ChunkData
import com.smallraw.chain.bitcoincore.script.Script
import com.smallraw.chain.bitcoincore.transaction.serializers.TransactionSerializer
import com.smallraw.chain.lib.core.extensions.hexToByteArray
import com.smallraw.chain.lib.core.extensions.toHex
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * ## 花费 P2PKH 的 UTXO##
 *
 * 签字获取 Hash 时对应的 UTXO 输入的脚本中放入锁定脚本
 * OP_DUP OP_HASH160 <UTXO 持有者的公钥 HASH160 的哈希> OP_EQUALVERIFY OP_CHECKSIG
 *
 * 使用 UTXO 对应的持有者私钥对交易 Hash 签字获得签名
 *
 * 对应的 UTXO 输入的脚本中放入解锁脚本
 * <签名> <UTXO 持有者的公钥>
 *
 *
 *
 * ## 支付到 P2PKH ##
 *
 * 在交易输出中填写锁定脚本
 * OP_DUP OP_HASH160 <UTXO 持有者的公钥 HASH160 的哈希> OP_EQUALVERIFY OP_CHECKSIG
 *
 */
@RunWith(AndroidJUnit4::class)
class SpendP2PKHTransactionUnitTest {

    @Test
    fun spend_p2pkh_to_p2pkh() {
        val network = TestNet()

        val convert = AddressConverter.default(network)

        val p2pkPrivateKey =
            PrivateKey("81c70e36ffa5e3e6425dc19c7c35315d3d72dc60b79cb78fe009a335de29dd22".hexToByteArray())
        val p2pkPublicKey = p2pkPrivateKey.getPublicKey()
        val fromAddress = p2pkPublicKey.getAddress(network)

        val txin =
            Transaction.Input(
                "fb48f4e23bf6ddf606714141ac78c3e921c8c0bebeb7c8abb2c799e9ff96ce6c".hexToByteArray(),
                0,
            )
        val redeemScript = fromAddress.lockScript()

        val toAddr1 = convert.convert("n4bkvTyU1dVdzsrhWBqBw8fEMbHjJvtmJR")
        val toAddr2 = convert.convert("mmYNBho9BWQB2dSniP1NJvnPoj5EVWw89w")
        val txout1 = Transaction.Output(10000000, toAddr1.lockScript())
        val txout2 = Transaction.Output(29000000, toAddr2.lockScript())

        val tx = Transaction(arrayOf(txin), arrayOf(txout1, txout2))

        Log.e(
            "TransactionUnitTest",
            "\nRaw unsigned transaction:\n" + TransactionSerializer.serialize(tx).toHex()
        )

        val txDigest = TransactionSerializer.hashForSignature(tx, 0, redeemScript)
        val sig = p2pkPrivateKey.sign(txDigest)
        txin.script = Script(ChunkData { sig.signature() }, ChunkData { p2pkPublicKey.getKey() })

        Log.e(
            "TransactionUnitTest",
            "\nRaw signed transaction:\n" + TransactionSerializer.serialize(tx).toHex()
        )

        Assert.assertArrayEquals(
            TransactionSerializer.serialize(tx, false),
            "02000000016cce96ffe999c7b2abc8b7bebec0c821e9c378ac41417106f6ddf63be2f448fb0000000000ffffffff0280969800000000001976a914fd337ad3bf81e086d96a68e1f8d6a0a510f8c24a88ac4081ba01000000001976a91442151d0c21442c2b038af0ad5ee64b9d6f4f4e4988ac00000000".hexToByteArray()
        )

        Assert.assertArrayEquals(
            TransactionSerializer.serialize(tx),
            "02000000016cce96ffe999c7b2abc8b7bebec0c821e9c378ac41417106f6ddf63be2f448fb000000006a473044022044ef433a24c6010a90af14f7739e7c60ce2c5bc3eab96eaee9fbccfdbb3e272202205372a617cb235d0a0ec2889dbfcadf15e10890500d184c8dda90794ecdf79492012103a2fef1829e0742b89c218c51898d9e7cb9d51201ba2bf9d9e9214ebb6af32708ffffffff0280969800000000001976a914fd337ad3bf81e086d96a68e1f8d6a0a510f8c24a88ac4081ba01000000001976a91442151d0c21442c2b038af0ad5ee64b9d6f4f4e4988ac00000000".hexToByteArray()
        )
    }
}