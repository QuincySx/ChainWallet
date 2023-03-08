package com.smallraw.chain.bitcoincore.transaction

import android.util.Log
import com.smallraw.chain.bitcoincore.PrivateKey
import com.smallraw.chain.bitcoincore.addressConvert.AddressConverter
import com.smallraw.chain.bitcoincore.network.TestNet
import com.smallraw.chain.bitcoincore.script.Chunk
import com.smallraw.chain.bitcoincore.script.OP_CHECKSIG
import com.smallraw.chain.bitcoincore.script.Script
import com.smallraw.chain.bitcoincore.transaction.serializers.TransactionSerializer
import com.smallraw.crypto.core.extensions.hexToByteArray
import com.smallraw.crypto.core.extensions.toHex
import org.junit.Assert
import org.junit.Test

/**
 * ## 花费 P2PK 的 UTXO ##
 *
 * 签字获取 Hash 时对应的 UTXO 输入的脚本中放入锁定脚本
 * <UTXO 持有者的公钥> OP_CHECKSIG
 *
 * 使用 UTXO 对应的持有者私钥对交易 Hash 签字获得签名
 *
 * 对应的 UTXO 输入的脚本中放入解锁脚本
 * <签名>
 *
 *
 *
 * ## 支付到 P2PK ##
 *
 * 在交易输出中填写锁定脚本
 * <UTXO 持有者的公钥> OP_CHECKSIG
 */

class SpendP2PKTransactionUnitTest {

    @Test
    fun spend_p2sh_to_p2pkh() {
        // 已经无法测试

        val network = TestNet()

        val convert = AddressConverter.default(network)

        val p2pkPrivateKey =
            PrivateKey("81c70e36ffa5e3e6425dc19c7c35315d3d72dc60b79cb78fe009a335de29dd22".hexToByteArray())
        val p2pkPublicKey = p2pkPrivateKey.getPublicKey()

        val txin = Transaction.Input(
            "3e8a4f00ac22cc73cf9591d05fa958a09e2b8264a8fde70060db16267af2888c".hexToByteArray(),
            0
        )
        val redeemScript = Script(Chunk(p2pkPublicKey.getKey()), Chunk(OP_CHECKSIG))

        val toAddr = convert.convert("myPAE9HwPeKHh8FjKwBNBaHnemApo3dw6e")
        val txout = Transaction.Output(4000L, toAddr.scriptPubKey())

        val tx = Transaction(arrayOf(txin), arrayOf(txout))

        Log.e(
            "TransactionUnitTest",
            "\nRaw unsigned transaction:\n" + TransactionSerializer.serialize(tx).toHex()
        )

        val txDigest = TransactionSerializer.hashForSignature(tx, 0, redeemScript)
        val sig = p2pkPrivateKey.sign(txDigest)
        txin.script = Script(Chunk(sig.signature()))

        Log.e(
            "TransactionUnitTest",
            "\nRaw signed transaction:\n" + TransactionSerializer.serialize(tx).toHex()
        )

        Assert.assertArrayEquals(
            TransactionSerializer.serialize(tx, false),
            "02000000018c88f27a2616db6000e7fda864822b9ea058a95fd09195cf73cc22ac004f8a3e0000000000ffffffff01a00f0000000000001976a914c3f8e5b0f8455a2b02c29c4488a550278209b66988ac00000000".hexToByteArray()
        )

        Assert.assertArrayEquals(
            TransactionSerializer.serialize(tx),
            "02000000018c88f27a2616db6000e7fda864822b9ea058a95fd09195cf73cc22ac004f8a3e000000004847304402204ed76ee6fe890e52e4bc3f980e8fab4a76229ad28afe777afccbb3dbf680e5bc022070a53ff75cfeecffce0c3d43ee1c5cd796b7727f136afd462def7b303c1141b201ffffffff01a00f0000000000001976a914c3f8e5b0f8455a2b02c29c4488a550278209b66988ac00000000".hexToByteArray()
        )
    }
}