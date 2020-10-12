package com.smallraw.chain.bitcoincore.transaction

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smallraw.chain.bitcoincore.PrivateKey
import com.smallraw.chain.bitcoincore.addressConvert.AddressConverter
import com.smallraw.chain.bitcoincore.network.TestNet
import com.smallraw.chain.bitcoincore.script.Chunk
import com.smallraw.chain.bitcoincore.script.Script
import com.smallraw.chain.bitcoincore.transaction.serializers.TransactionSerializer
import com.smallraw.crypto.core.extensions.hexToByteArray
import com.smallraw.crypto.core.extensions.toHex
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
        // Testnet3 测试交易 ID
        // 9515327f74dcaffd76ea6103f7a0714d56a60c522c9e86c8edc634f6942733c8
        // https://live.blockcypher.com/btc-testnet/tx/9515327f74dcaffd76ea6103f7a0714d56a60c522c9e86c8edc634f6942733c8/

        val network = TestNet()

        val convert = AddressConverter.default(network)

        val p2pkPrivateKey =
            PrivateKey("81c70e36ffa5e3e6425dc19c7c35315d3d72dc60b79cb78fe009a335de29dd22".hexToByteArray())
        val p2pkPublicKey = p2pkPrivateKey.getPublicKey()
        val fromAddress = p2pkPublicKey.getAddress(network)

        val txin = Transaction.Input(
            "a0a2eccb1b20d3779d92c671a9a01e9b64d15da3cccc1b11a68caa5b505d45dc".hexToByteArray(),
            0,
        )
        val redeemScript = fromAddress.scriptPubKey()

        val toAddr1 = convert.convert("myPAE9HwPeKHh8FjKwBNBaHnemApo3dw6e")
        val txout1 = Transaction.Output(4000L, toAddr1.scriptPubKey())

        val tx = Transaction(arrayOf(txin), arrayOf(txout1))

        Log.e(
            "TransactionUnitTest",
            "\nRaw unsigned transaction:\n" + TransactionSerializer.serialize(tx).toHex()
        )

        val txDigest = TransactionSerializer.hashForSignature(tx, 0, redeemScript)
        val sig = p2pkPrivateKey.sign(txDigest)
        txin.script = Script(Chunk(sig.signature()), Chunk(p2pkPublicKey.getKey()))

        Log.e(
            "TransactionUnitTest",
            "\nRaw signed transaction:\n" + TransactionSerializer.serialize(tx).toHex()
        )

        Assert.assertArrayEquals(
            TransactionSerializer.serialize(tx, false),
            "0200000001dc455d505baa8ca6111bcccca35dd1649b1ea0a971c6929d77d3201bcbeca2a00000000000ffffffff01a00f0000000000001976a914c3f8e5b0f8455a2b02c29c4488a550278209b66988ac00000000".hexToByteArray()
        )

        Assert.assertArrayEquals(
            TransactionSerializer.serialize(tx),
            "0200000001dc455d505baa8ca6111bcccca35dd1649b1ea0a971c6929d77d3201bcbeca2a0000000006a47304402200dcca69bf3f4eb3fe08773597d5917561fa592ac1b27e20c4c0e8acabc3e5258022011a64833883bf66f4b6b012dd2d8855a23b0147e0dcc35c719b58ebc7539ca92012103a2fef1829e0742b89c218c51898d9e7cb9d51201ba2bf9d9e9214ebb6af32708ffffffff01a00f0000000000001976a914c3f8e5b0f8455a2b02c29c4488a550278209b66988ac00000000".hexToByteArray()
        )
    }
}