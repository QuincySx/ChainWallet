package com.smallraw.chain.bitcoincore.transaction

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.smallraw.chain.bitcoincore.PrivateKey
import com.smallraw.chain.bitcoincore.Signature
import com.smallraw.chain.bitcoincore.addressConvert.AddressConverter
import com.smallraw.chain.bitcoincore.network.MainNet
import com.smallraw.chain.bitcoincore.network.TestNet
import com.smallraw.chain.bitcoincore.script.Chunk
import com.smallraw.chain.bitcoincore.script.OP_0
import com.smallraw.chain.bitcoincore.script.OP_1
import com.smallraw.chain.bitcoincore.script.OP_2
import com.smallraw.chain.bitcoincore.script.OP_CHECKMULTISIG
import com.smallraw.chain.bitcoincore.script.Script
import com.smallraw.chain.bitcoincore.script.ScriptType
import com.smallraw.chain.bitcoincore.script.SigHash
import com.smallraw.chain.bitcoincore.stream.BitcoinInputStream
import com.smallraw.chain.bitcoincore.transaction.serializers.TransactionSerializer
import com.smallraw.chain.lib.core.crypto.Sha256
import com.smallraw.chain.lib.core.extensions.hexToByteArray
import com.smallraw.chain.lib.core.extensions.toHex
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.experimental.or

/**
 * P2SH
 * + P2SH的含义是，向与该哈希匹配的脚本支付。我们简称为[支付脚本]。
 *
 * P2SH P2WSH [支付脚本]
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
class SpendP2SHP2WSHTransactionUnitTest {

    @Test
    fun spend_p2sh_p2wsh_to_p2pkh() {
        // 测试交易 ID
        // a0a2eccb1b20d3779d92c671a9a01e9b64d15da3cccc1b11a68caa5b505d45dc

        val network = TestNet()
        val convert = AddressConverter.default(network)

        val priv1 =
            PrivateKey("0cc4bc599c758dcdcc38515f923693e04873bfcfce0a60d1ba4693ab4fbd6c89".hexToByteArray())
        val priv2 =
            PrivateKey("69b33e2ee0f0cc5620df24fc804f338c8735098953387151e01903c0dada0661".hexToByteArray())

        // P2SH p2wsh 的赎回脚本
        val witnessScript = Script(
            Chunk(OP_1),
            Chunk(priv1.getPublicKey().getKey()),
            Chunk(priv2.getPublicKey().getKey()),
            Chunk(OP_2),
            Chunk(OP_CHECKMULTISIG)
        )

        val redeemScript = Script(
            Chunk(OP_0),
            Chunk(Sha256.sha256(witnessScript.scriptBytes))
        )

        val paymentAddress = convert.convert(redeemScript, ScriptType.P2SHWSH)

        val txinPrevAmount = 10000L
        val txin = Transaction.Input(
            hash = "3067e496f347004e61eaaba5bd68496600e44426f8a24bc0a36943a1f98bca27".hexToByteArray(),
            index = 1
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
            witnessScript,
            txinPrevAmount
        )
        val sig = priv1.sign(txDigest)

        tx.inputs[0].script = Script(Chunk(redeemScript.scriptBytes))
        // P2SH p2wpkh 的解锁脚本
        tx.inputs[0].witness.addStack(Chunk(byteArrayOf()))
        tx.inputs[0].witness.addStack(Chunk(sig.signature()))
        tx.inputs[0].witness.addStack(Chunk(witnessScript.scriptBytes))


        Log.e(
            "TransactionUnitTest",
            "\nRaw signed transaction:\n" + TransactionSerializer.serialize(tx).toHex()
        )

        Assert.assertArrayEquals(
            TransactionSerializer.serialize(tx, false),
            "020000000127ca8bf9a14369a3c04ba2f82644e400664968bda5abea614e0047f396e467300100000000ffffffff0288130000000000001976a914c3f8e5b0f8455a2b02c29c4488a550278209b66988aca00f00000000000017a9148007c86b6db162835370bca469cf487dc49c2ac58700000000".hexToByteArray()
        )

        Assert.assertArrayEquals(
            TransactionSerializer.serialize(tx),
            "0200000000010127ca8bf9a14369a3c04ba2f82644e400664968bda5abea614e0047f396e467300100000023220020c4b3ccbc954a24abfd903289f411c8af1b1f4b246abb818fae66f4450b45b208ffffffff0288130000000000001976a914c3f8e5b0f8455a2b02c29c4488a550278209b66988aca00f00000000000017a9148007c86b6db162835370bca469cf487dc49c2ac587030047304402201b399fc1a5a066fd176cae7bd67f2decccbb23773b7e77b2e8a17ce8b2b4f830022066325d919398941e1af3b13cce8a188d81ccb6bcc39600d948086d9c41c350a0014751210320c0c2020719cb638180f287ca59adc61fa7c201cfba789c95176c752bef9b4e2103aa49feb2409baba4c18197aaf8640d9cfd3a73aac7e4f13558017ca41bf2dd1752ae00000000".hexToByteArray()
        )
    }

    private fun sign(
        priv: PrivateKey,
        tx: Transaction,
        script: Script,
        txinPrevAmount: Long,
        signType: Byte = SigHash.ALL,
        txIndex: Int = 0,
    ): Signature {
        val txDigest2 = TransactionSerializer.hashForWitnessSignature(
            tx,
            txIndex,
            script,
            txinPrevAmount,
            signType
        )
        return priv.sign(txDigest2, signType)
    }

    /**
     * 官方测试用例
     * see https://github.com/bitcoin/bips/blob/master/bip-0143.mediawiki#P2SHP2WSH
     */
    @Test
    fun spend_main_p2sh_p2wsh_to_p2pkh() {
        val network = MainNet()
        val convert = AddressConverter.default(network)

        val priv1 =
            PrivateKey("730fff80e1413068a05b57d6a58261f07551163369787f349438ea38ca80fac6".hexToByteArray())
        val priv2 =
            PrivateKey("11fa3d25a17cbc22b29c44a484ba552b5a53149d106d3d853e22fdd05a2d8bb3".hexToByteArray())
        val priv3 =
            PrivateKey("77bf4141a87d55bdd7f3cd0bdccf6e9e642935fec45f2f30047be7b799120661".hexToByteArray())
        val priv4 =
            PrivateKey("14af36970f5025ea3e8b5542c0f8ebe7763e674838d08808896b63c3351ffe49".hexToByteArray())
        val priv5 =
            PrivateKey("fe9a95c19eef81dde2b95c1284ef39be497d128e2aa46916fb02d552485e0323".hexToByteArray())
        val priv6 =
            PrivateKey("428a7aee9f0c2af0cd19af3cf1c78149951ea528726989b2e83e4778d2c3f890".hexToByteArray())

        // P2SH p2wsh 的赎回脚本
        val witnessScript =
            Script("56210307b8ae49ac90a048e9b53357a2354b3334e9c8bee813ecb98e99a7e07e8c3ba32103b28f0c28bfab54554ae8c658ac5c3e0ce6e79ad336331f78c428dd43eea8449b21034b8113d703413d57761b8b9781957b8c0ac1dfe69f492580ca4195f50376ba4a21033400f6afecb833092a9a21cfdf1ed1376e58c5d1f47de74683123987e967a8f42103a6d48b1131e94ba04d9737d61acdaa1322008af9602b3b14862c07a1789aac162102d8b661b0b3302ee2f162b09e07a55ad5dfbe673a9f01d9f0c19617681024306b56ae".hexToByteArray())

        val redeemScript = Script(
            Chunk(OP_0),
            Chunk(Sha256.sha256(witnessScript.scriptBytes))
        )

        val txinPrevAmount = 987654321L
        val txin = Transaction.Input(
            hash = "36641869ca081e70f394c6948e8af409e18b619df2ed74aa106c1ca29787b96e".hexToByteArray()
                .reversedArray(),
            index = 1,
            sequence = 0xffffffff.toInt()
        )

        val txout1 = Transaction.Output(
            BitcoinInputStream("00e9a43500000000".hexToByteArray()).readInt64(),
            convert.convert(
                "389ffce9cd9ae88dcc0631e88a821ffdbe9bfe26".hexToByteArray(),
                ScriptType.P2PKH
            ).scriptPubKey()
        )
        val txOut2 = Transaction.Output(
            BitcoinInputStream("c0832f0500000000".hexToByteArray()).readInt64(),
            convert.convert(
                "7480a33f950689af511e6e84c138dbbd3c3ee415".hexToByteArray(),
                ScriptType.P2PKH
            ).scriptPubKey()
        )

        val tx = Transaction(
            arrayOf(txin),
            arrayOf(txout1, txOut2),
            version = 1,
            lockTime = 0x00000000
        )

        Log.e(
            "TransactionUnitTest",
            "\nRaw unsigned transaction:\n" + TransactionSerializer.serialize(tx).toHex()
        )

        val sig1 = sign(priv1, tx, witnessScript, txinPrevAmount, SigHash.ALL)
        val sig2 = sign(priv2, tx, witnessScript, txinPrevAmount, SigHash.NONE)
        val sig3 = sign(priv3, tx, witnessScript, txinPrevAmount, SigHash.SINGLE)
        val sig4 =
            sign(priv4, tx, witnessScript, txinPrevAmount, SigHash.ALL or SigHash.ANYONECANPAY)
        val sig5 =
            sign(priv5, tx, witnessScript, txinPrevAmount, SigHash.NONE or SigHash.ANYONECANPAY)
        val sig6 =
            sign(priv6, tx, witnessScript, txinPrevAmount, SigHash.SINGLE or SigHash.ANYONECANPAY)


        tx.inputs[0].script = Script(Chunk(redeemScript.scriptBytes))
        // P2SH p2wpkh 的解锁脚本
        tx.inputs[0].witness.addStack(Chunk(byteArrayOf()))
        tx.inputs[0].witness.addStack(Chunk(sig1.signature()))
        tx.inputs[0].witness.addStack(Chunk(sig2.signature()))
        tx.inputs[0].witness.addStack(Chunk(sig3.signature()))
        tx.inputs[0].witness.addStack(Chunk(sig4.signature()))
        tx.inputs[0].witness.addStack(Chunk(sig5.signature()))
        tx.inputs[0].witness.addStack(Chunk(sig6.signature()))
        tx.inputs[0].witness.addStack(Chunk(witnessScript.scriptBytes))


        Log.e(
            "TransactionUnitTest",
            "\nRaw signed transaction:\n" + TransactionSerializer.serialize(tx).toHex()
        )

        Assert.assertArrayEquals(
            TransactionSerializer.serialize(tx, false),
            "010000000136641869ca081e70f394c6948e8af409e18b619df2ed74aa106c1ca29787b96e0100000000ffffffff0200e9a435000000001976a914389ffce9cd9ae88dcc0631e88a821ffdbe9bfe2688acc0832f05000000001976a9147480a33f950689af511e6e84c138dbbd3c3ee41588ac00000000".hexToByteArray()
        )

        Assert.assertArrayEquals(
            TransactionSerializer.serialize(tx),
            "0100000000010136641869ca081e70f394c6948e8af409e18b619df2ed74aa106c1ca29787b96e0100000023220020a16b5755f7f6f96dbd65f5f0d6ab9418b89af4b1f14a1bb8a09062c35f0dcb54ffffffff0200e9a435000000001976a914389ffce9cd9ae88dcc0631e88a821ffdbe9bfe2688acc0832f05000000001976a9147480a33f950689af511e6e84c138dbbd3c3ee41588ac080047304402206ac44d672dac41f9b00e28f4df20c52eeb087207e8d758d76d92c6fab3b73e2b0220367750dbbe19290069cba53d096f44530e4f98acaa594810388cf7409a1870ce01473044022068c7946a43232757cbdf9176f009a928e1cd9a1a8c212f15c1e11ac9f2925d9002205b75f937ff2f9f3c1246e547e54f62e027f64eefa2695578cc6432cdabce271502473044022059ebf56d98010a932cf8ecfec54c48e6139ed6adb0728c09cbe1e4fa0915302e022007cd986c8fa870ff5d2b3a89139c9fe7e499259875357e20fcbb15571c76795403483045022100fbefd94bd0a488d50b79102b5dad4ab6ced30c4069f1eaa69a4b5a763414067e02203156c6a5c9cf88f91265f5a942e96213afae16d83321c8b31bb342142a14d16381483045022100a5263ea0553ba89221984bd7f0b13613db16e7a70c549a86de0cc0444141a407022005c360ef0ae5a5d4f9f2f87a56c1546cc8268cab08c73501d6b3be2e1e1a8a08824730440220525406a1482936d5a21888260dc165497a90a15669636d8edca6b9fe490d309c022032af0c646a34a44d1f4576bf6a4a74b67940f8faa84c7df9abe12a01a11e2b4783cf56210307b8ae49ac90a048e9b53357a2354b3334e9c8bee813ecb98e99a7e07e8c3ba32103b28f0c28bfab54554ae8c658ac5c3e0ce6e79ad336331f78c428dd43eea8449b21034b8113d703413d57761b8b9781957b8c0ac1dfe69f492580ca4195f50376ba4a21033400f6afecb833092a9a21cfdf1ed1376e58c5d1f47de74683123987e967a8f42103a6d48b1131e94ba04d9737d61acdaa1322008af9602b3b14862c07a1789aac162102d8b661b0b3302ee2f162b09e07a55ad5dfbe673a9f01d9f0c19617681024306b56ae00000000".hexToByteArray()
        )
    }
}