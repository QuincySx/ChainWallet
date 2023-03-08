package com.smallraw.chain.bitcoincore.transaction

import android.util.Log
import com.smallraw.chain.bitcoincore.PrivateKey
import com.smallraw.chain.bitcoincore.addressConvert.AddressConverter
import com.smallraw.chain.bitcoincore.network.TestNet
import com.smallraw.chain.bitcoincore.script.*
import com.smallraw.chain.bitcoincore.transaction.serializers.TransactionSerializer
import com.smallraw.crypto.core.extensions.hexToByteArray
import com.smallraw.crypto.core.extensions.toHex
import org.junit.Assert
import org.junit.Test

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
 **/

class SpendP2SHMultipleTransactionUnitTest {

    /**
     * 花费 P2SH 的转账
     */
    @Test
    fun spend_p2sh_multiple_to_p2pkh() {
        // Testnet3 测试交易 ID
        // 4e6350a180d875b7c1287533c06efeebeab942d363b511b415d7f67006f2a764
        // https://live.blockcypher.com/btc-testnet/tx/4e6350a180d875b7c1287533c06efeebeab942d363b511b415d7f67006f2a764/

        val network = TestNet()
        val convert = AddressConverter.default(network)

        val priv1 =
            PrivateKey("471817cfb2eedbc9a3e10b89d5fc3e8a5cdbc13fe58f08c6d9013df5060b2a3c".hexToByteArray())
        val priv2 =
            PrivateKey("71516154a21724d57fdcdb242cca34a324ffff9b3e4befcc7f375bc5e896250f".hexToByteArray())

        // P2SH 的多签脚本
        val lockScript = Script(
            Chunk(OP_2),
            Chunk("03d728ad6757d4784effea04d47baafa216cf474866c2d4dc99b1e8e3eb936e730".hexToByteArray()),
            Chunk("02d83bba35a8022c247b645eed6f81ac41b7c1580de550e7e82c75ad63ee9ac2fd".hexToByteArray()),
            Chunk("03aeb681df5ac19e449a872b9e9347f1db5a0394d2ec5caf2a9c143f86e232b0d9".hexToByteArray()),
            Chunk(OP_3),
            Chunk(OP_CHECKMULTISIG),
        )

        val paymentAddress = convert.convert(lockScript, ScriptType.P2SH)

        val txin = Transaction.Input(
            "032da8f4b94015ef84e758719e8cf98f636409fbcbb2fd95bfae2e1faa4ad77a".hexToByteArray(),
            0,
        )

        val payeeAddress = convert.convert("myPAE9HwPeKHh8FjKwBNBaHnemApo3dw6e")

        val txout1 = Transaction.Output(9000L, payeeAddress.scriptPubKey())

        val tx = Transaction(arrayOf(txin), arrayOf(txout1))

        Log.e(
            "TransactionUnitTest",
            "\nRaw unsigned transaction:\n" + TransactionSerializer.serialize(tx).toHex()
        )

        val txDigest = TransactionSerializer.hashForSignature(tx, 0, lockScript)
        val sig1 = priv1.sign(txDigest)
        val sig2 = priv2.sign(txDigest)

        // P2SH 的解锁脚本
        txin.script = Script(
            Chunk(OP_0),
            Chunk(sig1.signature()),
            Chunk(sig2.signature()),
            Chunk(lockScript.scriptBytes)
        )

        Log.e(
            "TransactionUnitTest",
            "\nRaw signed transaction:\n" + TransactionSerializer.serialize(tx).toHex()
        )

        Assert.assertArrayEquals(
            TransactionSerializer.serialize(tx, false),
            "02000000017ad74aaa1f2eaebf95fdb2cbfb0964638ff98c9e7158e784ef1540b9f4a82d030000000000ffffffff0128230000000000001976a914c3f8e5b0f8455a2b02c29c4488a550278209b66988ac00000000".hexToByteArray()
        )

        Assert.assertArrayEquals(
            TransactionSerializer.serialize(tx),
            "02000000017ad74aaa1f2eaebf95fdb2cbfb0964638ff98c9e7158e784ef1540b9f4a82d0300000000fdfe0000483045022100a6a432163aa421d9823104b630f60a4c61f9f9455e50bda00a060b1e6c8ee10502206ae3b4f92ec84cc694542f11f127d9216d96cab683e9d66c88d6b679191c4f7e01483045022100a48b2acaae471a6a9f6e51396b40ad09aaabba4c602457d7efa5dd74aa6640750220288eb7185e43a3a1e3a2be2c2a2ccf4d0a3852a1529d22a89e1cde28c98169aa014c69522103d728ad6757d4784effea04d47baafa216cf474866c2d4dc99b1e8e3eb936e7302102d83bba35a8022c247b645eed6f81ac41b7c1580de550e7e82c75ad63ee9ac2fd2103aeb681df5ac19e449a872b9e9347f1db5a0394d2ec5caf2a9c143f86e232b0d953aeffffffff0128230000000000001976a914c3f8e5b0f8455a2b02c29c4488a550278209b66988ac00000000".hexToByteArray()
        )
    }
}