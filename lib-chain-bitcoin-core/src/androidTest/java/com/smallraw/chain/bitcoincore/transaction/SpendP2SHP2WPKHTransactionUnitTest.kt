package com.smallraw.chain.bitcoincore.transaction

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smallraw.chain.bitcoincore.PrivateKey
import com.smallraw.chain.bitcoincore.addressConvert.AddressConverter
import com.smallraw.chain.bitcoincore.network.MainNet
import com.smallraw.chain.bitcoincore.script.Chunk
import com.smallraw.chain.bitcoincore.script.OP_0
import com.smallraw.chain.bitcoincore.script.OP_CHECKSIG
import com.smallraw.chain.bitcoincore.script.OP_DUP
import com.smallraw.chain.bitcoincore.script.OP_EQUALVERIFY
import com.smallraw.chain.bitcoincore.script.OP_HASH160
import com.smallraw.chain.bitcoincore.script.Script
import com.smallraw.chain.bitcoincore.script.ScriptType
import com.smallraw.chain.bitcoincore.stream.BitcoinInputStream
import com.smallraw.chain.bitcoincore.transaction.serializers.TransactionSerializer
import com.smallraw.chain.lib.core.extensions.hexToByteArray
import com.smallraw.chain.lib.core.extensions.toHex
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * P2SH
 * + P2SH的含义是，向与该哈希匹配的脚本支付。我们简称为[支付脚本]。
 *
 * P2SH Multiple [支付脚本]
 * OP_M <PK1> <PK2> <PK3> OP_N CHECKMULTISIG
 * 例如 2-3 的支付脚本，这笔钱有两个用户的签字即刻花费该 P2SH。
 * OP_2 <PK1> <PK2> <PK3> OP_3 CHECKMULTISIG
 *
 *
 *
 * ## 花费 P2SH Multiple 2-3 的 UTXO ##
 *
 * 签字获取 Hash 时对应的 UTXO 输入的脚本中放入支付脚本
 * OP_M <PK1> <PK2> <PK3> OP_N CHECKMULTISIG
 *
 * 使用 UTXO 对应的持有者私钥对交易 Hash 签字获得签名
 *
 * 对应的 UTXO 输入的脚本中放入解锁脚本,以下三种解锁脚本均可
 * OP_0 <签名1> <签名2> <支付脚本>
 * OP_0 <签名1> <签名3> <支付脚本>
 * OP_0 <签名2> <签名3> <支付脚本>
 * 前面放 OP_0 是因为 BUG，后来成为共识。
 *
 *
 *
 * ## 支付到 P2SH Multiple ##
 *
 * 在交易输出中填写锁定脚本
 * OP_HASH160 <支付脚本 的 HASH160> OP_EQUAL
 *
 *
 *
 * ## P2SH 使用注意 ##
 * P2SH 的地址由 [支付脚本] HASH160 得来，由于花费 P2SH 地址上的 UTXO 时，所以 [支付脚本] 需要自行保存。
 */
@RunWith(AndroidJUnit4::class)
class SpendP2SHP2WPKHTransactionUnitTest {

    /**
     * 花费 P2SH 的转账
     */
    @Test
    fun spend_p2sh_p2wpkh_to_p2pkh() {
        val network = MainNet()
        val convert = AddressConverter.default(network)

        val priv1 =
            PrivateKey("eb696a065ef48a2192da5b28b694f87544b30fae8327c4510137a922f32c6dcf".hexToByteArray())
        val paymentPub = priv1.getPublicKey()

        // P2SH 的 p2wpkh 脚本
        val lockScript = Script(
            Chunk(OP_DUP),
            Chunk(OP_HASH160),
            Chunk(paymentPub.getHash()),
            Chunk(OP_EQUALVERIFY),
            Chunk(OP_CHECKSIG)
        )

        val txin = Transaction.Input(
            hash = "db6b1b20aa0fd7b23880be2ecbd4a98130974cf4748fb66092ac4d3ceb1a5477".hexToByteArray()
                .reversedArray(),
            index = 1,
            sequence = 0xfffffffe.toInt()
        )

        val payeeScript1 = Script(
            Chunk(OP_DUP),
            Chunk(OP_HASH160),
            Chunk("a457b684d7f0d539a46a45bbc043f35b59d0d963".hexToByteArray()),
            Chunk(OP_EQUALVERIFY),
            Chunk(OP_CHECKSIG)
        )

        val payeeScript2 = Script(
            Chunk(OP_DUP),
            Chunk(OP_HASH160),
            Chunk("fd270b1ee6abcaea97fea7ad0402e8bd8ad6d77c".hexToByteArray()),
            Chunk(OP_EQUALVERIFY),
            Chunk(OP_CHECKSIG)
        )

        val txout1 = Transaction.Output(199996600, payeeScript1)
        val txout2 = Transaction.Output(800000000, payeeScript2)

        val tx =
            Transaction(
                arrayOf(txin),
                arrayOf(txout1, txout2),
                version = 1,
                lockTime = BitcoinInputStream("92040000".hexToByteArray()).readInt32()
            )

        Log.e(
            "TransactionUnitTest",
            "\nRaw unsigned transaction:\n" + TransactionSerializer.serialize(tx).toHex()
        )


        val txDigest = TransactionSerializer.hashForWitnessSignature(
            tx,
            0,
            lockScript,
            BitcoinInputStream("00ca9a3b00000000".hexToByteArray()).readInt64()
        )
        val sig = priv1.sign(txDigest)

        val redeemScript = Script(
            Chunk(OP_0),
            Chunk(paymentPub.getHash())
        )

        tx.inputs[0].script = Script(Chunk(redeemScript.scriptBytes))
        // P2SH p2wpkh 的解锁脚本
        tx.inputs[0].witness.addStack(Chunk(sig.signature()))
        tx.inputs[0].witness.addStack(Chunk(paymentPub.getKey()))


        Log.e(
            "TransactionUnitTest",
            "\nRaw signed transaction:\n" + TransactionSerializer.serialize(tx).toHex()
        )

        Log.e(
            "TransactionUnitTest",
            "\nRaw signed transaction:\n" + tx
        )

//        Assert.assertArrayEquals(
//            TransactionSerializer.serialize(tx, false),
//            "0200000001df2b060fa2e5e9c8ed5eaf6a45c13753ec8c63282b2688322eba40cd98ea067a0000000000ffffffff01b02e052a010000001976a9147faf0c785828c1f87fca32ef071066f60ea100d188ac00000000".hexToByteArray()
//        )

        Assert.assertArrayEquals(
            TransactionSerializer.serialize(tx),
            "01000000000101db6b1b20aa0fd7b23880be2ecbd4a98130974cf4748fb66092ac4d3ceb1a5477010000001716001479091972186c449eb1ded22b78e40d009bdf0089feffffff02b8b4eb0b000000001976a914a457b684d7f0d539a46a45bbc043f35b59d0d96388ac0008af2f000000001976a914fd270b1ee6abcaea97fea7ad0402e8bd8ad6d77c88ac02473044022047ac8e878352d3ebbde1c94ce3a10d057c24175747116f8288e5d794d12d482f0220217f36a485cae903c713331d877c1f64677e3622ad4010726870540656fe9dcb012103ad1d8e89212f0b92c74d23bb710c00662ad1470198ac48c43f7d6f93a2a2687392040000".hexToByteArray()
        )
    }
}