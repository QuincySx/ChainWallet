package com.smallraw.chain.bitcoincore.transaction

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smallraw.chain.bitcoincore.PrivateKey
import com.smallraw.chain.bitcoincore.addressConvert.AddressConverter
import com.smallraw.chain.bitcoincore.network.TestNet
import com.smallraw.chain.bitcoincore.script.*
import com.smallraw.chain.bitcoincore.transaction.serializers.TransactionSerializer
import com.smallraw.chain.lib.core.extensions.hexToByteArray
import com.smallraw.chain.lib.core.extensions.toHex
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * ## 花费 P2WPKH 的 UTXO##
 *
 * 签字获取 Hash 时对应的 UTXO 输入的脚本中放入锁定脚本
 * OP_DUP OP_HASH160 <UTXO 持有者的公钥 HASH160 的哈希> OP_EQUALVERIFY OP_CHECKSIG
 *
 * 使用 UTXO 对应的持有者私钥对交易 Hash 签字获得签名
 *
 * 对应的 UTXO 输入的隔离见证信息中放入解锁脚本，此处不是以脚本的方式放入的。
 * <签名> <UTXO 持有者的公钥>
 *
 *
 *
 * ## 支付到 P2WPKH ##
 *
 * 在交易输出中填写锁定脚本
 * OP_DUP OP_HASH160 <UTXO 持有者的公钥 HASH160 的哈希> OP_EQUALVERIFY OP_CHECKSIG
 *
 */
@RunWith(AndroidJUnit4::class)
class SpendP2WPKHTransactionUnitTest {
    @Test
    fun test_spend_P2WPKH_to_p2wsh() {
        val network = TestNet()
        val convert = AddressConverter.default(network)

        val paymentPriv =
            PrivateKey("0cc4bc599c758dcdcc38515f923693e04873bfcfce0a60d1ba4693ab4fbd6c89".hexToByteArray())
        val payeePriv =
            PrivateKey("0cc4bc599c758dcdcc38515f923693e04873bfcfce0a60d1ba4693ab4fbd6c89".hexToByteArray())

        val paymentPub = paymentPriv.getPublicKey()
        val changeAddress = paymentPub.getAddress(network, true)
        val redeemScript = Script(
            Chunk { OP_DUP },
            Chunk { OP_HASH160 },
            ChunkData { paymentPub.getHash() },
            Chunk { OP_EQUALVERIFY },
            Chunk { OP_CHECKSIG }
        )

        val payeeP2SHLockScript = Script(
            Chunk { OP_1 },
            ChunkData { payeePriv.getPublicKey().getKey() },
            Chunk { OP_1 },
            Chunk { OP_CHECKMULTISIG })
        val toAddress = convert.convert(payeeP2SHLockScript,ScriptType.P2WSH)

        val txinPrevAmount = 1764912L
        val txin = Transaction.Input(
            "d222d91e2da368ac38e84aa615c557e4caeacce02aa5dbca10d840fd460fc938".hexToByteArray(),
            0
        )

        val txOut1 = Transaction.Output(10000, toAddress.lockScript())
        // change 找零
        val txOut2 = Transaction.Output(1744912, changeAddress.lockScript())

        val tx = Transaction(arrayOf(txin), arrayOf(txOut1, txOut2))

        Log.e(
            "TransactionUnitTest",
            "\nRaw unsigned transaction:\n" + TransactionSerializer.serialize(tx).toHex()
        )

        val txDigest =
            TransactionSerializer.hashForWitnessSignature(tx, 0, redeemScript, txinPrevAmount)
        val sig = paymentPriv.sign(txDigest)
        tx.inputs[0].witness.addStack(sig.signature())
        tx.inputs[0].witness.addStack(paymentPub.getKey())

        Log.e(
            "TransactionUnitTest",
            "\nRaw signed transaction:\n" + TransactionSerializer.serialize(tx).toHex()
        )

        Assert.assertArrayEquals(
            TransactionSerializer.serialize(tx, false, false),
            "020000000138c90f46fd40d810cadba52ae0cceacae457c515a64ae838ac68a32d1ed922d20000000000ffffffff021027000000000000220020f8e25a3c8f3dbf1de4720297961a5a62f1710a13cfb2938523a29295cd24012a10a01a000000000016001442660b0e164f17fffd36129d762163355bac69fa00000000".hexToByteArray()
        )

        Assert.assertArrayEquals(
            TransactionSerializer.serialize(tx),
            "0200000000010138c90f46fd40d810cadba52ae0cceacae457c515a64ae838ac68a32d1ed922d20000000000ffffffff021027000000000000220020f8e25a3c8f3dbf1de4720297961a5a62f1710a13cfb2938523a29295cd24012a10a01a000000000016001442660b0e164f17fffd36129d762163355bac69fa02483045022100ed78a0c4185a5a89f5961f55c8e972060fc8754858787c747dc105af850884d3022038e3d8e013e6ac519a916c6e77cc4f528aeb11e15f7cd96c7e4c468fa7506f2301210320c0c2020719cb638180f287ca59adc61fa7c201cfba789c95176c752bef9b4e00000000".hexToByteArray()
        )
    }
}