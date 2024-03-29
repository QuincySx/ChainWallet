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
 * ## 花费 P2WSH Multiple 2-3 的 UTXO ##
 *
 * 签字获取 Hash 时对应的 UTXO 输入的脚本中放入支付脚本
 * OP_M <PK1> <PK2> <PK3> OP_N CHECKMULTISIG
 *
 * 使用 UTXO 对应的持有者私钥对交易 Hash 签字获得签名
 *
 * 对应的 UTXO 输入的隔离见证信息中放入解锁脚本,以下三种解锁脚本均可，此处不是以脚本的方式放入的。
 * OP_0 <签名1> <签名2> <支付脚本>
 * OP_0 <签名1> <签名3> <支付脚本>
 * OP_0 <签名2> <签名3> <支付脚本>
 * 前面放 OP_0 是因为 BUG，后来成为共识。
 *
 *
 *
 * ## 支付到 P2WSH Multiple ##
 *
 * 在交易输出中填写锁定脚本
 * OP_HASH160 <支付脚本 的 HASH160> OP_EQUAL
 *
 */

class SpendP2WSHTransactionUnitTest {
    @Test
    fun test_spend_p2wsh_to_p2wpkh() {
        // Testnet3 测试交易 ID
        // cdc29c11a029fe6a6804381fa6c8d280915f062c62d4cbdd9db6664acbfe8b2d
        // https://live.blockcypher.com/btc-testnet/tx/cdc29c11a029fe6a6804381fa6c8d280915f062c62d4cbdd9db6664acbfe8b2d/

        val network = TestNet()
        val convert = AddressConverter.default(network)

        val priv1 =
            PrivateKey("0cc4bc599c758dcdcc38515f923693e04873bfcfce0a60d1ba4693ab4fbd6c89".hexToByteArray())
        val priv2 =
            PrivateKey("69b33e2ee0f0cc5620df24fc804f338c8735098953387151e01903c0dada0661".hexToByteArray())

        val redeemScript = Script(
            Chunk(OP_1),
            Chunk(priv1.getPublicKey().getKey()),
            Chunk(priv2.getPublicKey().getKey()),
            Chunk(OP_2),
            Chunk(OP_CHECKMULTISIG)
        )

        val paymentAddress = convert.convert(redeemScript, ScriptType.P2WSH)
        val payeeAddress = convert.convert("tb1qtstf97nhk2gycz7vl37esddjpxwt3ut30qp5pn")

        val txinPrevAmount = 10000L
        val txin = Transaction.Input(
            "daf349128c241f1cbd612594168dcde7669f86eddb7c6a139d47cb5fbb6b99f2".hexToByteArray(),
            1
        )

        val txOut1 = Transaction.Output(5000L, payeeAddress.scriptPubKey())
        // change 找零
        val txOut2 = Transaction.Output(3000L, paymentAddress.scriptPubKey())

        val tx = Transaction(arrayOf(txin), arrayOf(txOut1, txOut2))

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
        val sig1 = priv1.sign(txDigest)

        tx.inputs[0].witness.addStack(byteArrayOf())
        tx.inputs[0].witness.addStack(sig1.signature())
        tx.inputs[0].witness.addStack(redeemScript.scriptBytes)

        Log.e(
            "TransactionUnitTest",
            "\nRaw signed transaction:\n" + TransactionSerializer.serialize(tx).toHex()
        )

        System.err.println("TransactionUnitTest $tx")

        Assert.assertArrayEquals(
            TransactionSerializer.serialize(tx, false, false),
            "0200000001f2996bbb5fcb479d136a7cdbed869f66e7cd8d16942561bd1c1f248c1249f3da0100000000ffffffff0288130000000000001600145c1692fa77b2904c0bccfc7d9835b2099cb8f171b80b000000000000220020c4b3ccbc954a24abfd903289f411c8af1b1f4b246abb818fae66f4450b45b20800000000".hexToByteArray()
        )

        Assert.assertArrayEquals(
            TransactionSerializer.serialize(tx),
            "02000000000101f2996bbb5fcb479d136a7cdbed869f66e7cd8d16942561bd1c1f248c1249f3da0100000000ffffffff0288130000000000001600145c1692fa77b2904c0bccfc7d9835b2099cb8f171b80b000000000000220020c4b3ccbc954a24abfd903289f411c8af1b1f4b246abb818fae66f4450b45b2080300483045022100facddc1426dc99e68aacbe8b38d8a797518b16f104459349714f96d940ab523c022018825a5101a14b7871d0eca183993024a69f0d757adc8d2cddeb93843b83f431014751210320c0c2020719cb638180f287ca59adc61fa7c201cfba789c95176c752bef9b4e2103aa49feb2409baba4c18197aaf8640d9cfd3a73aac7e4f13558017ca41bf2dd1752ae00000000".hexToByteArray()
        )
    }
}