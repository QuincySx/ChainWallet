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

class SpendP2WPKHTransactionUnitTest {
    @Test
    fun test_spend_P2WPKH_to_p2wsh() {
        // Testnet3 测试交易 ID
        // 1b7264bd29e3d1f7e44307c49a781e21ee98cef67ff1523839425e5a05ce07bc
        // https://live.blockcypher.com/btc-testnet/tx/1b7264bd29e3d1f7e44307c49a781e21ee98cef67ff1523839425e5a05ce07bc/

        val network = TestNet()
        val convert = AddressConverter.default(network)

        val paymentPriv =
            PrivateKey("0cc4bc599c758dcdcc38515f923693e04873bfcfce0a60d1ba4693ab4fbd6c89".hexToByteArray())
        val payeePriv =
            PrivateKey("0cc4bc599c758dcdcc38515f923693e04873bfcfce0a60d1ba4693ab4fbd6c89".hexToByteArray())

        val paymentPub = paymentPriv.getPublicKey()
        val changeAddress = paymentPub.getAddress(network, true)
        val redeemScript = Script(
            Chunk(OP_DUP),
            Chunk(OP_HASH160),
            Chunk(paymentPub.getHash()),
            Chunk(OP_EQUALVERIFY),
            Chunk(OP_CHECKSIG)
        )

        val payeeP2SHLockScript = Script(
            Chunk(OP_1),
            Chunk(payeePriv.getPublicKey().getKey()),
            Chunk(OP_1),
            Chunk(OP_CHECKMULTISIG)
        )
        val toAddress = convert.convert(payeeP2SHLockScript, ScriptType.P2WSH)

        val txinPrevAmount = 10000L
        val txin = Transaction.Input(
            "a46144a078c715fcecfc81e3c7dce359a7a40a4506e06b8dfa53acf69a60da25".hexToByteArray(),
            0
        )

        val txOut1 = Transaction.Output(5000L, toAddress.scriptPubKey())
        // change 找零
        val txOut2 = Transaction.Output(4000L, changeAddress.scriptPubKey())

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
            "020000000125da609af6ac53fa8d6be006450aa4a759e3dcc7e381fcecfc15c778a04461a40000000000ffffffff028813000000000000220020f8e25a3c8f3dbf1de4720297961a5a62f1710a13cfb2938523a29295cd24012aa00f00000000000016001442660b0e164f17fffd36129d762163355bac69fa00000000".hexToByteArray()
        )

        Assert.assertArrayEquals(
            TransactionSerializer.serialize(tx),
            "0200000000010125da609af6ac53fa8d6be006450aa4a759e3dcc7e381fcecfc15c778a04461a40000000000ffffffff028813000000000000220020f8e25a3c8f3dbf1de4720297961a5a62f1710a13cfb2938523a29295cd24012aa00f00000000000016001442660b0e164f17fffd36129d762163355bac69fa02483045022100eb02de6ea8b69e0cc31edb3a290e4cdac06302e93def1331475438ab2cf0308702207547f98b9b174c3d07bcc310ca1c4e3eeae95aa20b8742e73b03905ed321efd701210320c0c2020719cb638180f287ca59adc61fa7c201cfba789c95176c752bef9b4e00000000".hexToByteArray()
        )
    }
}