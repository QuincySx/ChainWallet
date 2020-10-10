package com.smallraw.chain.bitcoincore.transaction

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smallraw.chain.bitcoincore.PrivateKey
import com.smallraw.chain.bitcoincore.addressConvert.AddressConverter
import com.smallraw.chain.bitcoincore.network.MainNet
import com.smallraw.chain.bitcoincore.network.TestNet
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
 * P2SH P2WPKH [支付脚本]
 * OP_0 <PK1>
 *
 *
 *
 * ## 花费 P2SH P2WPKH UTXO ##
 *
 * 签字获取 Hash 时对应的 UTXO 输入的脚本中放入赎回脚本
 * OP_DUP OP_HASH160 <UTXO 持有者的公钥 HASH160 的哈希> OP_EQUALVERIFY OP_CHECKSIG
 *
 * 使用 UTXO 对应的持有者私钥对交易 Hash 签字获得签名
 *
 * 对应的 UTXO 输入的脚本中放入解锁脚本,以下三种解锁脚本均可
 * OP_0 <PK1>
 * 对应的 UTXO 输入的隔离见证信息中放入解锁脚本，此处不是以脚本的方式放入的。
 * <签名> <UTXO 持有者的公钥>
 *
 *
 *
 * ## 支付到 P2SH P2WPKH ##
 *
 * 在交易输出中填写锁定脚本
 * OP_HASH160 <支付脚本 的 HASH160> OP_EQUAL
 *
 */
@RunWith(AndroidJUnit4::class)
class SpendP2SHP2WPKHTransactionUnitTest {

    @Test
    fun spend_p2sh_p2wpkh_to_p2pkh() {
        // Testnet3 测试交易 ID
        // 3e8a4f00ac22cc73cf9591d05fa958a09e2b8264a8fde70060db16267af2888c
        // https://live.blockcypher.com/btc-testnet/tx/3e8a4f00ac22cc73cf9591d05fa958a09e2b8264a8fde70060db16267af2888c/

        val network = TestNet()
        val convert = AddressConverter.default(network)

        val priv1 =
            PrivateKey("eb696a065ef48a2192da5b28b694f87544b30fae8327c4510137a922f32c6dcf".hexToByteArray())
        val paymentPub = priv1.getPublicKey()

        val paymentAddress = convert.convert(paymentPub, ScriptType.P2SHWPKH)

        val txinPrevAmount = 10000L
        val txin = Transaction.Input(
            hash = "94cdec055687b1802ba73ac00e0fb63aeff7ca7f70c65ce7908a0353b5ae2423".hexToByteArray(),
            index = 0
        )

        // P2SH p2wpkh 的赎回脚本
        val redeemScript = Script(
            Chunk(OP_DUP),
            Chunk(OP_HASH160),
            Chunk(paymentPub.getHash()),
            Chunk(OP_EQUALVERIFY),
            Chunk(OP_CHECKSIG)
        )

        val payeeAddress = convert.convert("myPAE9HwPeKHh8FjKwBNBaHnemApo3dw6e")

        val txout1 = Transaction.Output(5000L, payeeAddress.scriptPubKey())
        // change 找零
        val txOut2 = Transaction.Output(4000L, paymentAddress.scriptPubKey())

        val tx = Transaction(
            arrayOf(txin),
            arrayOf(txout1, txOut2)
        )

        Log.e(
            "TransactionUnitTest",
            "\nRaw unsigned transaction:\n" + TransactionSerializer.serialize(tx).toHex()
        )

        val txDigest = TransactionSerializer.hashForWitnessSignature(
            tx,
            0,
            redeemScript,
            txinPrevAmount
        )
        val sig = priv1.sign(txDigest)

        val scriptSig = Script(
            Chunk(OP_0),
            Chunk(paymentPub.getHash())
        )

        tx.inputs[0].script = Script(Chunk(scriptSig.scriptBytes))
        // P2SH p2wpkh 的解锁脚本
        tx.inputs[0].witness.addStack(Chunk(sig.signature()))
        tx.inputs[0].witness.addStack(Chunk(paymentPub.getKey()))


        Log.e(
            "TransactionUnitTest",
            "\nRaw signed transaction:\n" + TransactionSerializer.serialize(tx).toHex()
        )

        Assert.assertArrayEquals(
            TransactionSerializer.serialize(tx, false),
            "02000000012324aeb553038a90e75cc6707fcaf7ef3ab60f0ec03aa72b80b1875605eccd940000000000ffffffff0288130000000000001976a914c3f8e5b0f8455a2b02c29c4488a550278209b66988aca00f00000000000017a9144733f37cf4db86fbc2efed2500b4f4e49f3120238700000000".hexToByteArray()
        )

        Assert.assertArrayEquals(
            TransactionSerializer.serialize(tx),
            "020000000001012324aeb553038a90e75cc6707fcaf7ef3ab60f0ec03aa72b80b1875605eccd94000000001716001479091972186c449eb1ded22b78e40d009bdf0089ffffffff0288130000000000001976a914c3f8e5b0f8455a2b02c29c4488a550278209b66988aca00f00000000000017a9144733f37cf4db86fbc2efed2500b4f4e49f3120238702483045022100b16365776df6178f7b0ef443bff024b654f143676b29b2cdeef68dae2e86e02602207f0ffd4695ee09d1dc509a3df78f3c2588d0f6c79254fc09cb14d373c04fbf65012103ad1d8e89212f0b92c74d23bb710c00662ad1470198ac48c43f7d6f93a2a2687300000000".hexToByteArray()
        )
    }

    /**
     * 官方测试用例
     * see https://github.com/bitcoin/bips/blob/master/bip-0143.mediawiki#P2SHP2WSH
     */
    @Test
    fun spend_main_p2sh_p2wpkh_to_p2pkh() {
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

        val txout1 = Transaction.Output(
            199996600,
            convert.convert(
                "a457b684d7f0d539a46a45bbc043f35b59d0d963".hexToByteArray(),
                ScriptType.P2PKH
            ).scriptPubKey()
        )
        val txout2 = Transaction.Output(
            800000000,
            convert.convert(
                "fd270b1ee6abcaea97fea7ad0402e8bd8ad6d77c".hexToByteArray(),
                ScriptType.P2PKH
            ).scriptPubKey()
        )

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

        Assert.assertArrayEquals(
            TransactionSerializer.serialize(tx, false),
            "0100000001db6b1b20aa0fd7b23880be2ecbd4a98130974cf4748fb66092ac4d3ceb1a54770100000000feffffff02b8b4eb0b000000001976a914a457b684d7f0d539a46a45bbc043f35b59d0d96388ac0008af2f000000001976a914fd270b1ee6abcaea97fea7ad0402e8bd8ad6d77c88ac92040000".hexToByteArray()
        )

        Assert.assertArrayEquals(
            TransactionSerializer.serialize(tx),
            "01000000000101db6b1b20aa0fd7b23880be2ecbd4a98130974cf4748fb66092ac4d3ceb1a5477010000001716001479091972186c449eb1ded22b78e40d009bdf0089feffffff02b8b4eb0b000000001976a914a457b684d7f0d539a46a45bbc043f35b59d0d96388ac0008af2f000000001976a914fd270b1ee6abcaea97fea7ad0402e8bd8ad6d77c88ac02473044022047ac8e878352d3ebbde1c94ce3a10d057c24175747116f8288e5d794d12d482f0220217f36a485cae903c713331d877c1f64677e3622ad4010726870540656fe9dcb012103ad1d8e89212f0b92c74d23bb710c00662ad1470198ac48c43f7d6f93a2a2687392040000".hexToByteArray()
        )
    }
}